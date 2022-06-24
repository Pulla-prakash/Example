package com.vcare.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vcare.beans.Department;
import com.vcare.beans.Hospital;
import com.vcare.beans.Patients;
import com.vcare.service.HospitalService;
import com.vcare.service.PatientsService;

@Controller
@RequestMapping("/hospital")
public class HospitalController {

	@Autowired
	HospitalService hospitalService;
	@Autowired
	PatientsService patientsService;

	static Logger log = Logger.getLogger(HospitalController.class.getClass());

	@GetMapping("/hospitalList")
	public String getAllHospitals(Model model) {
		log.info("inside getAllHospitals this will get the all hospitals:::");
		List<Hospital> hospList = hospitalService.getAllHospitals();
		List<Hospital> list = new ArrayList<>();
		for (Hospital obj : hospList) {
			if (obj.getIsactive() == 'Y' || obj.getIsactive() == 'y') {
				list.add(obj);
			}
		}
		model.addAttribute("hospitalsList", list);
		return "hospitalslist";

	}

	// Appointment
	@GetMapping("/hospitalList/{id}")
	public String getAllHospitals(Model model, @PathVariable("id") int id, HttpServletRequest request) {
		log.info("inside getAllHospitals this will get the all hospitals:::");
		List<Hospital> hospList = hospitalService.getAllHospitals();
		model.addAttribute("hospitalsList", hospList);
		Patients Pobj = patientsService.getPatientById(id);
		model.addAttribute("pid", Pobj.getPatientId());

		// Session for patient appointment
		request.getSession().setAttribute("patientid", Pobj.getPatientId());

		return "phospitallist";

	}

	@RequestMapping(value = "/addHospital", method = RequestMethod.GET)
	public String newHospital(Model model, Hospital objHospital) {
		model.addAttribute("objHospital", objHospital);
		return "hospital";
	}

	@RequestMapping(value = "/savehospital", method = RequestMethod.POST)
	public String addHospital(Model model, Hospital objHospital) {
		objHospital.setIsactive('Y');
		objHospital.setCreated(LocalDate.now());

		hospitalService.addHospital(objHospital);
		model.addAttribute("objHospital", objHospital);
		return "redirect:/hospital/hospitalList";
	}

	@GetMapping("/editHosp/{id}")
	public String getHospitalById(Model model, @PathVariable("id") int hospitalId) {
		Hospital objHospital = hospitalService.getHospitalById(hospitalId);
		log.info("inside getHospitalById id is:::" + objHospital.getHospitalId());
		objHospital.setCreated(LocalDate.now());
		model.addAttribute("objHospital", objHospital);
		model.addAttribute("objHosp", objHospital.getHospitalName());
		model.addAttribute("hospitalId", hospitalId);
		return "hospital";
	}

	@GetMapping("/deleteHosp/{id}")
	public String deleteHospital(Model model, @PathVariable int id) {
		log.info("inside deleteHospital id:::" + id);
		Hospital inactive = hospitalService.getHospitalById(id);
		inactive.setIsactive('N');
		hospitalService.updateHospital(inactive);
		List<Hospital> hospList = hospitalService.getAllHospitals();
		model.addAttribute("hospitalsList", hospList);

		return "redirect:/hospital/hospitalList";

	}
}
