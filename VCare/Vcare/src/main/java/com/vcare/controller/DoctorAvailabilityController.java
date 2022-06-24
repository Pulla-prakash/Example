package com.vcare.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcare.beans.Doctor;
import com.vcare.beans.DoctorAvailability;
import com.vcare.beans.HospitalBranch;
import com.vcare.service.DoctorAvailabilityService;
import com.vcare.service.DoctorService;
import com.vcare.service.HospitalBranchService;

import ch.qos.logback.core.net.SyslogOutputStream;

@Controller
@RequestMapping("/")
public class DoctorAvailabilityController {
	@Autowired
	DoctorAvailabilityService doctorAvailabilityService;
	@Autowired
	DoctorService docService;
	
	@Autowired
	HospitalBranchService hospitalBranchService;

	@GetMapping(value = "/availabilityList")
	public String getAllDoctorAvailability(Model model) {
		List<DoctorAvailability> availabilityList = doctorAvailabilityService.getAllDoctorAvailability();
		model.addAttribute("availabilityList", availabilityList);
		return "availabilitylist";
	}

	@RequestMapping(value = "/addAvailability/{did}", method = RequestMethod.GET)
	public String newAvailability(Model model, @PathVariable("did") int id,DoctorAvailability doctorAvailability) {
		
		Doctor doc=docService.GetDocotorById(id);
		List<HospitalBranch >branchList=new  ArrayList<>();
		List<HospitalBranch > hbList=hospitalBranchService.getAllHospitalbranch();
		String dbId=doc.getHospitalBranchId();
		String[] arrOfStr = dbId.split(",");
		System.out.println("arrOfStr="+arrOfStr);
		for (String a : arrOfStr)
				for(HospitalBranch hb:hbList) {
			if(hb.getHospitalBranchId()==Integer.parseInt(a)) {
				branchList.add(hb);
			}
		}
		
		model.addAttribute("branchList", branchList);
		model.addAttribute("doc", doc.getDoctorId());
		System.out.println("wertyuiopsdfghjk"+doc);
		model.addAttribute("id", id);
		return "doctoravailability";
	}

	@RequestMapping(value = "/saveAvailability/{id}", method = RequestMethod.POST)
	public String addDoctorAvailability(Model model, @PathVariable("id") int id,DoctorAvailability doctorAvailability,
			@RequestParam("startTiming") String date1, @RequestParam("endTiming") String date2) {
		doctorAvailability.setStartTimings(date1);
		doctorAvailability.setEndTimings(date2);
		Doctor doc=docService.GetDocotorById(id);
		model.addAttribute("doc", doc.getDoctorName());
		System.out.println("123456789123456783245678"+doc.getDoctorName());
		doctorAvailabilityService.addDoctorAvailability(doctorAvailability);
		List<DoctorAvailability> availabilityList = doctorAvailabilityService.getAllDoctorAvailability();
		model.addAttribute("availabilityList", availabilityList);
		return "availabilitylist";
	}

	@GetMapping("/editAvailability/{id}")
	public String getAvailabilityById(Model model, @PathVariable("id") int availabilityId) {
		DoctorAvailability doctorAvailability = doctorAvailabilityService.getDoctorAvailabilityId(availabilityId);
		System.out.println(doctorAvailability.getAvailabilityId());
		model.addAttribute("doctorAvailability", doctorAvailability);
		return "doctoravailability";
	}

	@GetMapping("/deleteAvailability/{id}")
	public String deleteAvailability(Model model, @PathVariable int id) {
		doctorAvailabilityService.deleteEmployeeById(id);
		List<DoctorAvailability> availabilityList = doctorAvailabilityService.getAllDoctorAvailability();
		model.addAttribute("availabilityList", availabilityList);
		return "availabilitylist";
	}
}
