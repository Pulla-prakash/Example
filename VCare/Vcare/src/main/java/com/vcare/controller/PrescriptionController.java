package com.vcare.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

import com.vcare.beans.Appointment;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.Patients;
import com.vcare.beans.Prescription;
import com.vcare.service.AppointmentService;
import com.vcare.service.HospitalBranchService;
import com.vcare.service.PatientsService;
import com.vcare.service.PrescriptionService;

@Controller
public class PrescriptionController {

	@Autowired
	PrescriptionService prescriptionService;

	@Autowired
	PatientsService patientService;

	@Autowired

	AppointmentService appointmentService;
	@Autowired
	HospitalBranchService hospitalBranchService;

	static Logger log = Logger.getLogger(PrescriptionController.class.getClass());

	@GetMapping("/prescription")
	public String prescriptionlist(Model model) {

		List<Prescription> prescription = prescriptionService.getAllPrescription();

		List<Prescription> list = new ArrayList<>();
		for (Prescription pre : prescription) {
			if (pre.getIsactive() == 'Y' || pre.getIsactive() == 'y') {
				list.add(pre);
			}
		}
		model.addAttribute("prescription", list);
		return "prescriptionlist";
	}

	@RequestMapping(value = "/addprescription/{aid}", method = RequestMethod.GET)
	public String newInsurance(Model model, @PathVariable("aid") long aid,
			@ModelAttribute(value = "prescriptionobj") Prescription prescription) {
		// Patients patient=patientService.getPatientById(id);
		// log.info("patientID:::"+patient.getPatientId());

		Appointment appointment = appointmentService.getAppointmentById(aid);
		log.info("appointment ---ID:::" + appointment.getAppointmentId());
		log.info("appointment:::" + appointment.getAppointmentId());
		 HospitalBranch hb=hospitalBranchService.getHospitalbranchId(appointment.getHospitalBranchId());
		 model.addAttribute("hositalBranch",hb);
		model.addAttribute("appointment", appointment);
		model.addAttribute("prescriptionobj", prescription);
		return "prescriptionform";
	}

	@RequestMapping(value = "/saveprescription", method = RequestMethod.POST)
	public String addPrescription(Model model, @ModelAttribute(value = "prescriptionobj") Prescription pres) {
		pres.setIsactive('Y');
		pres.setCreated(LocalDate.now());
		log.info(pres.getPrescriptionId());

		model.addAttribute("prescriptionobj", pres);
		prescriptionService.addPrescription(pres);

		List<Appointment> appointment = appointmentService.getAllAppointment();
		model.addAttribute("appointment", appointment);

		List<Prescription> prescriptionlist = prescriptionService.getAllPrescription();
		model.addAttribute("prescription", prescriptionlist);
		return "redirect:/prescription";
	}

	@GetMapping("/acceptprescription/{id}")
	public String getHospitalById(Model model, @PathVariable("id") int Id, Prescription appointment) {
		Prescription prescriptionlist = prescriptionService.getPrescriptionById(Id);
		log.info("inside getHospitalbranchId id is:::" + prescriptionlist.getPrescriptionId());
		model.addAttribute("hospitalBranchList", prescriptionlist);

		return "prescriptionform";
	}

	@GetMapping("/deleteprescription/{id}")
	public String deleteHospitalBranch(Model model, @PathVariable int id) {
		log.info("inside deleteHospitalBranch id:::" + id);
		Prescription inactive = prescriptionService.getPrescriptionById(id);
		inactive.setIsactive('N');

		prescriptionService.updatePrescription(inactive);
		List<Prescription> appointmentList = prescriptionService.getAllPrescription();
		model.addAttribute("hospitalBranchList", appointmentList);

		return "prescriptionlist";

	}

	@GetMapping("/editprescription/{id}")
	public String getById(Model model, @PathVariable("id") int prescriptionId) {

		Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
		log.info("Edit of department in ...");
		log.info("inside getHospitalbranchId id is:::" + prescriptionService.getPrescriptionById(prescriptionId));
		model.addAttribute("prescriptionobj", prescription);

		return "prescriptionform";

	}

	@GetMapping("/list/{pid}")
	public String getByIdList(Model model, @PathVariable("pid") int pid) {

		List<Prescription> prescription = prescriptionService.getByPid(pid);

		model.addAttribute("prescription", prescription);

		return "separatelist";
	}

	@GetMapping("/acceptpres/{pid}")
	public String getHospital(Model model, @PathVariable("pid") int pid, Prescription prescription) {

		Patients patient = patientService.getPatientById(pid);

		log.info("inside getHospitalbranchId id is:::" + patientService.getPatientById(pid));

		model.addAttribute("name", patient.getFirstName());
		model.addAttribute("lastname", patient.getLastName());
		model.addAttribute("age", patient.getPatientAge());
		model.addAttribute("address", patient.getPatientAddress());
		model.addAttribute("mobile", patient.getPatientMobile());
		// model.addAttribute("initial", patient.getLastName());
		// model.addAttribute("initial", prescription.appointment.doctor.doctorName);

		List<Prescription> prescriptions = prescriptionService.getAllPrescription();

		for (Prescription obj : prescriptions) {
			if (obj.appointment.getPatient().getPatientId() == pid) {

				model.addAttribute("initial", obj.appointment.getDoctor().getDoctorName());
			//	model.addAttribute("hospitalname",
					//	obj.appointment.getDoctor().getHospitalbranch().getHospitalBranchName());
		//		model.addAttribute("prescription", obj.getPrescription());
				return "normal";
			}
		}

		return "normal";
	}

	@GetMapping("/noo")

	public String normal() {

		return "normal";
	}

}
