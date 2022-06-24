package com.vcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vcare.beans.Ambulance;
import com.vcare.beans.HospitalBranch;
import com.vcare.service.AmbulanceService;
import com.vcare.service.HospitalBranchService;

@Controller
public class AmbulanceController {

	@Autowired
	AmbulanceService ambulaceservice;
	@Autowired
	HospitalBranchService hospitalBranchService;

	@GetMapping("/AmbulanceList")
	public String getAllAmbulance(Model model) {
		List<Ambulance> AmbList = ambulaceservice.getAllAmbulance();
		model.addAttribute("ambulanceList", AmbList);
		return "ambulanceList";
	}

	@GetMapping("/addAmbulance/{branchId}")
	public String showNewForm(Model model, Ambulance ambulance, @PathVariable("branchId") int branchId) {
		HospitalBranch hospitalbranch = hospitalBranchService.getHospitalbranchId(branchId);
		model.addAttribute("branchId", hospitalbranch.getHospitalBranchId());
		model.addAttribute("ambulanceobj", ambulance);
		return "ambulanceForm";
	}

	@RequestMapping(value = "/saveAmbulance", method = RequestMethod.POST)
	public String addAmbulance(Model model, Ambulance ambulance) {
		model.addAttribute("ambulanceobj", ambulance);
		ambulaceservice.addAmbulance(ambulance);
		return "redirect:/AmbulanceList";
	}

	@GetMapping("/deleteAmbulance/{id}")
	public String deleteService(Model model, @PathVariable("id") int ambulanceId) {
		System.out.println("inside deleteHospitalBranch id:::" + ambulanceId);
		ambulaceservice.deleteAmbulancById(ambulanceId);
		List<Ambulance> AmbList = ambulaceservice.getAllAmbulance();
		model.addAttribute("ambulanceList", AmbList);
		return "redirect:/AmbulanceList";
	}

	@GetMapping("/editAmbulance/{id}")
	public String getById(Model model, @PathVariable("id") int ambulanceId) {
		Ambulance ambulance = ambulaceservice.getById(ambulanceId);
		model.addAttribute("ambulanceobj", ambulance);
		System.out.println("inside getHospitalbranchId id is:::" + ambulaceservice.getById(ambulanceId));
		ambulaceservice.UpdateAmbulance(ambulance);
		// model.addAttribute("employeeObj", objSecEmployee);
		return "ambulanceForm";
	}

}
