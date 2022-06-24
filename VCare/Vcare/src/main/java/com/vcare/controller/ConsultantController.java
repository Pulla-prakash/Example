package com.vcare.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vcare.beans.ConsultantFee;
import com.vcare.beans.Doctor;
import com.vcare.service.ConsultantService;
import com.vcare.service.DoctorService;

@Controller
@RequestMapping("/consultant")
public class ConsultantController {

	@Autowired

	ConsultantService Consultantservice;
	@Autowired
	DoctorService doctorservice;
	static Logger log = Logger.getLogger(AppointmentController.class.getClass());

	@GetMapping("/Feelist{id}")

	public String viewAllDoctrs(Model model, @PathVariable("id") int id) {
		log.info("===========feelist method=============");
		Doctor objDoc = doctorservice.GetDocotorById(id);
		log.info("this is feelist : " + objDoc.getDoctorId());
		model.addAttribute("id", objDoc);
		model.addAttribute("ConsultantList", Consultantservice.getAllconsultent());
		return "consultantlist";
	}

	@RequestMapping(value = "/AddFee/{id}", method = RequestMethod.GET)
	public String newconsultantfee(Model model, @PathVariable("id") int id, ConsultantFee consultantObj) {
		log.info("============Addfee method============");
		model.addAttribute("objconsultant", consultantObj);

		Doctor doctors = doctorservice.GetDocotorById(id);
		model.addAttribute("objId", doctors);

		model.addAttribute("Doctorname", doctors.getDoctorName());
		model.addAttribute("dotrId", doctors.getDoctorId());
		List<ConsultantFee> consultantfee = Consultantservice.getAllconsultent();
		model.addAttribute("ConsultantList", consultantfee);
		return "feeform";

	}

	@RequestMapping(value = "/saveFee", method = RequestMethod.POST)
	public String addFee(Model model, @ModelAttribute(value = "objconsultant") ConsultantFee consultantObj) {
		log.info("============= saveFee=========");
		log.info("new record:set id: :" + consultantObj.getConsultantId());
		consultantObj.setIsactive('y');
		consultantObj.setCreated(LocalDate.now());

		if (consultantObj.getConsultantId() != 0) {
			log.info("inside new doctor saving :::" + consultantObj.getConsultantId());
			Consultantservice.addconsultent(consultantObj);
		} else {
			Consultantservice.updateconsultentfee(consultantObj);
		}
		List<ConsultantFee> ConsultantList = Consultantservice.getAllconsultent();
		model.addAttribute("ConsultantList", ConsultantList);
		return "redirect:/consultant/Feelist/{id}";
	}

	@GetMapping("/Updateconsultant/{Id}")
	public String editConsultant(Model model, @PathVariable(value = "Id") int ConsultantId) {
		log.info("==========Updateconsultant================");
		ConsultantFee ConsultantObj = Consultantservice.GetconsultentById(ConsultantId);
		model.addAttribute("objconsultant", ConsultantObj);

		Doctor doctors = doctorservice.GetDocotorById(ConsultantId);
		log.info("consultantfee updated by consultantId  : " + ConsultantId);
		model.addAttribute("Doctorname", doctors.getDoctorName());
		model.addAttribute("dctrId", doctors.getDoctorId());
		model.addAttribute("objconsultant", ConsultantObj);
		model.addAttribute("ConsultantId", ConsultantId);
		return "feeform";
	}

	@GetMapping("/deleteconsultant/{Id}")
	public String deleteFee(Model model, @PathVariable(value = "Id") int ConsultantId) {
		log.info("=============== consultantfee delete method===========");
		Consultantservice.deleteconsultentById(ConsultantId);
		log.info(" deleted by consultantId no : " + ConsultantId);
		model.addAttribute("ConsultantList", Consultantservice.getAllconsultent());
		return "redirect:/consultant/Feelist";

	}

}
