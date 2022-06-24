package com.vcare.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcare.beans.Appointment;
import com.vcare.beans.Doctor;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.Patients;
import com.vcare.beans.Services;
import com.vcare.repository.DoctorRepository;
import com.vcare.repository.ServiceRepository;
import com.vcare.service.AppointmentService;
import com.vcare.service.DoctorService;
import com.vcare.service.EmailSenderService;
import com.vcare.service.HospitalBranchService;
import com.vcare.service.PatientsService;

@Controller
public class AppointmentController {

	@Autowired
	AppointmentService appointmentService;
	@Autowired
	DoctorService doctorService;
	@Autowired
	PatientsService patientsService;
	@Autowired
	HospitalBranchService hospitalBranchService;
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	ServiceRepository serviceRepository;
	
	static Logger log = Logger.getLogger(AppointmentController.class.getClass());

	@GetMapping(value = "/appointmentlist")
	public String appointmentlist(Model model) {
		List<Appointment> appointment = appointmentService.getAllAppointment();
		log.info("===========Appointment List=============");
		log.info("Appointment : " + appointment);
		List<Appointment> list = new ArrayList<>();
		for (Appointment app : appointment) {
			if (app.getIsactive() == 'Y' || app.getIsactive() == 'y') {
				list.add(app);
			}
		}
		model.addAttribute("appointmentlist", list);
		return "pappointmentlist";
	}

	// Appointments for Serparate doctors
	@GetMapping(value = "/appointmentlist/{dname}")
	public String pAppointmentlist(Model model, @PathVariable("dname") String dname, HttpServletRequest request) {
		List<Appointment> appointment = appointmentService.getAllAppointment();
		log.info("===========Appointment List=============");
		log.info("Appointment : " + appointment);
		List<Appointment> list = new ArrayList<>();
		for (Appointment app : appointment) {
			if (app.getIsactive() != 'N' && app.getIsactive() != 'n'
					&& app.getDoctor().getDoctorName().equalsIgnoreCase(dname)) {
				list.add(app);
			}
		}
		HttpSession session = request.getSession();
		session.setAttribute("drname", dname);
		model.addAttribute("appointmentlist", list);
		model.addAttribute("meetingLink","");
		return "pappointmentlist";
	}

	@GetMapping(value = "/deleteappointment/{aid}")
	public String deleteAppointment(@PathVariable("aid") int aid) {
		Appointment inactive = appointmentService.getAppointmentById(aid);
		inactive.setIsactive('N');
		appointmentService.updateAppointment(inactive);
		return "redirect:/appointmentlist/" + inactive.getDoctor().getDoctorName();
	}

	@RequestMapping(value = "/addappointment/{hbid}/{pid}/{did}", method = RequestMethod.GET)
	public String newPatientAppointmnet(Model model, @PathVariable("pid") int pid, @PathVariable("did") int did, @PathVariable("hbid") int hbid,
			@RequestParam("slot") String slot,@RequestParam("date")String date,HttpServletRequest request,	Appointment appointment, HospitalBranch hBranchEntry) {

		String dates=request.getParameter("date");
		System.out.println("DATE="+dates);
		String slots=request.getParameter("slot");
		model.addAttribute("slot",slots);
		model.addAttribute("date",dates);
		Doctor doctor = doctorService.GetDocotorById(did);
		String str=doctor.getHospitalBranchId();
		String[] arrhbid=str.split(",");
		System.out.println("model.addAttribute(\"hospitalBranchAddress\",hb.getHospitalBranchName());");
		HospitalBranch hb=hospitalBranchService.getHospitalbranchId(hbid);
		System.out.println("model.addAttribute(\"hospitalBranchAddress\",hb.getHospitalBranchName());"+hb.getHospitalBranchName());
		model.addAttribute("hospitalBranch",hb);
		for(String a:arrhbid) {
			if(a.equals(hbid)) {
				HospitalBranch hospitalBranch=hospitalBranchService.getHospitalbranchId(hbid);
				System.out.println("hospitalBranch.getHospitalBranchName()===="+hospitalBranch.getHospitalBranchName());
				//model.addAttribute("hospitalBranchName",hospitalBranch.getHospitalBranchName());
			}
		}
		List<HospitalBranch> branchList = hospitalBranchService.getAllHospitalbranch();
		model.addAttribute("did", doctor.getDoctorId());
		Patients patient = patientsService.getPatientById(pid);
		log.info("============Add Appointment============");
		log.info("AddAppointment : " + patient);
		log.info("AGE:::" + patient.getPatientAge());
		model.addAttribute("patientName", patient.getFirstName());
		model.addAttribute("patientage", patient.getPatientAge());
		model.addAttribute("patientgender", patient.getPatientGender());
		model.addAttribute("patientmobile", patient.getPatientMobile());

		// NEED BRANCH NAME
		int count = branchList.size();
		for (int i = 0; i < count; i++) {
			/*
			 * if (doctor.getHospitalBranchId()==branchList.get(i).getHospitalBranchId()) {
			 * 
			 * int hid = branchList.get(i).getHospitalBranchId(); hBranchEntry =
			 * hospitalBranchService.getHospitalbranchId(hid); log.info(hBranchEntry);
			 * break; }
			 */
		}
		//model.addAttribute("hospitalBranchName", hBranchEntry.getHospitalBranchName());
		model.addAttribute("hospitalBranchAddress", hBranchEntry.getHospitalBranchLocation());
		model.addAttribute("doctorName", doctor.getDoctorName());
		model.addAttribute("doctorFee", doctor.getDoctorFee());

		model.addAttribute("pid", patient.getPatientId());
		model.addAttribute("appointment", appointment);
		model.addAttribute("slot", slot);
		model.addAttribute("date", date);
		
		model.addAttribute("consultantType","");

		return "appointmentform";
	}

	@RequestMapping(value = "/saveappointment/{did}", method = RequestMethod.POST)
	public String addPatientAppointment(Model model, @PathVariable("did") int did, Appointment appointment) {
		appointment.setIsactive('Y');
		appointmentService.updateAppointment(appointment);
		log.info("Inserting :" + appointment);
		log.info("=========Patient appointment Inserted=======");
		return "redirect:/appointmentbooked/" + appointment.getPatient().getPatientId() + "/{did}/"
				+ appointment.getAppointmentId();
	}
	

	// AFTER ONLINE APPOINTMENT BOOKING
	@RequestMapping(value = "/appointmentbooked/{pid}/{id}/{appid}", method = RequestMethod.GET)
	public String patientAppointmnetBooked(Model model, @PathVariable("pid") int pid, @PathVariable("id") int id,
			@PathVariable("appid") int appid, Appointment appointmentObj, HospitalBranch hBranchEntry) {
		// message
		model.addAttribute("appmsg", "Your Appointment is Booked");
		Doctor doctor = doctorService.GetDocotorById(id);
		Appointment appointment = appointmentService.getAppointmentById(appid);
		List<HospitalBranch> branchList = hospitalBranchService.getAllHospitalbranch();
		model.addAttribute("did", doctor.getDoctorId());
		Patients patient = patientsService.getPatientById(pid);
		log.info("============Add Appointment============");
		log.info("AddAppointment : " + patient);
		model.addAttribute("patientName", patient.getFirstName());
		model.addAttribute("patientage", patient.getPatientAge());
		model.addAttribute("patientgender", patient.getPatientGender());
		model.addAttribute("patientmobile", patient.getPatientMobile());
		model.addAttribute("slot", appointment.getSlot());
		model.addAttribute("date", appointment.getDate());
		HospitalBranch hb=hospitalBranchService.getHospitalbranchId(appointment.getHospitalBranchId());
		model.addAttribute("hospitalBranch",hb);
		// NEED BRANCH NAME
		int count = branchList.size();
		for (int i = 0; i < count; i++) {
			/*
			 * if (doctor.getHospitalbranch().getHospitalBranchId() ==
			 * branchList.get(i).getHospitalBranchId()) {
			 * 
			 * int hid = branchList.get(i).getHospitalBranchId(); hBranchEntry =
			 * hospitalBranchService.getHospitalbranchId(hid); log.info(hBranchEntry);
			 * break; }
			 */
		}
		model.addAttribute("hospitalBranchName", hBranchEntry.getHospitalBranchName());
		model.addAttribute("hospitalBranchAddress", hBranchEntry.getHospitalBranchLocation());
		model.addAttribute("doctorName", doctor.getDoctorName());
		model.addAttribute("doctorFee", doctor.getDoctorFee());

		model.addAttribute("pid", patient.getPatientId());
		
		model.addAttribute("appointment", appointment);

		return "appointmentform";
	}
	
	@RequestMapping(value = "/offlinesaveappointment/{did}", method = RequestMethod.POST)
	public String addofflinePatientAppointment(Model model,@PathVariable("did") int did, Appointment appointment) {
	appointment.setIsactive('Y');
	appointmentService.updateAppointment(appointment);
	log.info("Inserting :"+appointment);
	log.info("=========Patient appointment Inserted=======");
	return "redirect:/offlineappointmentbooked/"+appointment.getPatient().getPatientId()+"/{did}/"+appointment.getAppointmentId();
	}
	
	
	
	//AFTER APPOINTMENT BOOKING
	@RequestMapping(value = "/offlineappointmentbooked/{pid}/{id}/{appid}", method = RequestMethod.GET)
	public String offlinepatientAppointmnetBooked(Model model, @PathVariable("pid") int pid, @PathVariable("id") int id,@PathVariable("appid") int appid,
	Appointment appointmentObj, HospitalBranch hBranchEntry) {
	//message
	model.addAttribute("appmsg", "Your Appointment is Booked");
	Doctor doctor = doctorService.GetDocotorById(id);
	List<HospitalBranch> branchList = hospitalBranchService.getAllHospitalbranch();
	model.addAttribute("did", doctor.getDoctorId());
	Patients patient = patientsService.getPatientById(pid);
	log.info("============Add Appointment============");
	log.info("AddAppointment : "+patient);
	Appointment appointment=appointmentService.getAppointmentById(appid);
	model.addAttribute("patientName", patient.getFirstName());
	model.addAttribute("patientage", patient.getPatientAge());
	model.addAttribute("patientgender", patient.getPatientGender());
	model.addAttribute("patientmobile", patient.getPatientMobile());
	// NEED BRANCH NAME
	int count = branchList.size();
	for (int i = 0; i < count; i++) {
	if (appointment.getHospitalBranchId() == branchList.get(i).getHospitalBranchId()) { int hid = branchList.get(i).getHospitalBranchId();
	hBranchEntry = hospitalBranchService.getHospitalbranchId(hid);
	log.info(hBranchEntry);
	log.info("hiddddddd"+hid);
	break;
	}
	}
	model.addAttribute("hospitalBranchName", hBranchEntry.getHospitalBranchName());
	model.addAttribute("hospitalBranchAddress", hBranchEntry.getHospitalBranchLocation());
	model.addAttribute("doctorName", doctor.getDoctorName());
	model.addAttribute("doctorFee", doctor.getDoctorFee()); model.addAttribute("pid", patient.getPatientId());
	
	model.addAttribute("date",appointment.getDate());
	model.addAttribute("slot",appointment.getSlot());
	model.addAttribute("consult",appointment.getConsultantType());
	model.addAttribute("appointment", appointment);
	return "offlineappointmentform";
	}



	@GetMapping("/acceptappointment/{id}")
	public String getPatientAppointmentById(Model model, @PathVariable("id") long Id, Appointment appointment) {
		Appointment appointmentlist = appointmentService.getAppointmentById(Id);
		log.info("Accepted Appointment : " + appointmentlist);
		model.addAttribute("appointmentlist", appointmentlist);

		return "appointmentform";
	}

	@GetMapping("/rejectappointment/{id}")
	public String deletePatientAppointment(Model model, @PathVariable long id) {
		System.out.println("inside deleteHospitalBranch id:::" + id);
		log.info("=============Reject Appointment===============");
		log.info("Rejected Appointment by id :" + id);
		appointmentService.deleteAppointmentById(id);
		List<Appointment> appointmentList = appointmentService.getAllAppointment();
		log.info("=============After Rejecting Appointment===============");
		log.info("Appointment List : " + appointmentList);
		model.addAttribute("appointmentlist", appointmentList);

		return "redirect:/appointmentlist";

	}
	@GetMapping(value="/insert/{pid}")
	public String createpage(Model model,@PathVariable long pid) {
		
		Appointment appointment = appointmentService.getAppointmentById(pid);
		log.info("valueeee"+appointment.getAppointmentId());
		model.addAttribute("pid", appointment.getAppointmentId());
		model.addAttribute("appointment", appointment);

		return "insert";
	}
	
	@PostMapping("/linksave/{pid}")
	public String link(Model model ,@PathVariable long pid,Appointment appointment ,Patients patientObj, final HttpServletRequest request,
			final HttpServletResponse response,HttpSession session) {
	System.out.println("getLink:"+appointment.getLink());
		Appointment appointments = appointmentService.getAppointmentById(pid);
		appointments.setLink(appointment.getLink());
		model.addAttribute("appointment", appointment);
		System.out.println("link of meeting::"+appointments.getLink());
		appointmentService.updateAppointment(appointments);
		model.addAttribute("meetingLink",appointment.getLink());
		
		SimpleMailMessage mailMessage = new SimpleMailMessage(); 
        mailMessage.setTo(appointments.getPatient().getPatientMailId());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("choppariabhilash@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"saveuser?patientMailId="+appointments.getLink());
   
        emailSenderService.sendEmail(mailMessage);
        model.addAttribute("Emailid",patientObj.getPatientMailId());
        System.out.println(":::::::::::::::::"+patientObj.getPatientMailId());
		return "redirect:/appointmentlist";
	}
	
	@GetMapping("bookAppointment/{pid}")
	public String bookAppointment(Model model,Appointment appointment,@PathVariable("pid") int pid,@RequestParam(name = "serviceList", required = false) List<String> serviceList) {
		
		Patients patient=patientsService.getPatientById(pid);
		if(patient.getFirstName()=="" || patient.getPatientMailId()=="" || patient.getPatientAge()==0 || patient.getPatientMobile()==0) {
			return "redirect:/patientprofile/{pid}";
		}
		List<String> serviceNames = serviceRepository.findDistinctService(serviceList);
		model.addAttribute("serviceNames",serviceNames);
		model.addAttribute("patient",patient);
		model.addAttribute("appointment",appointment);
		return "pappointmentform";
	}
	
	@RequestMapping("/selectedService")
	public String selectedService(Model model,Appointment appointment,
			final HttpServletRequest request, HttpSession session,@RequestParam(name = "serviceList", required = false) List<String> serviceList) {

		String getQry = request.getParameter("getQry");
		String serviceName = request.getParameter("serviceName");
		System.out.println(getQry + "++++++++" + serviceName);
		if (getQry != null && getQry.equals("selectedService") && serviceName != null && serviceName != "") {
			List<Services> servicelist = serviceRepository.findService(serviceName);//method in repository
			List<Integer> bid = new ArrayList<>();//for storing hospital PKs
			for (Services slist : servicelist) {
				bid.add(slist.getHospitalbranch().getHospitalBranchId());//Adds One by one id to list
			}
			List<HospitalBranch> hblist=new ArrayList<>();//To store rows of matched service branches
			for (Integer serid : bid) {
				//getting hospital by id
				HospitalBranch hospitalBranchById = hospitalBranchService.getHospitalbranchId(serid);
				//adding hospital list by id to list
				hblist.add(hospitalBranchById);
			}
			int pid=appointment.getPatient().getPatientId();
			Patients patient=patientsService.getPatientById(pid);
			List<String> serviceNames = serviceRepository.findDistinctService(serviceList);
			model.addAttribute("serviceNames",serviceNames);
			model.addAttribute("appointment",appointment);
			if(hblist.size()!=0) {
			model.addAttribute("hblist",hblist);
			}
			else {
				model.addAttribute("hblist","");
			}
//			session.setAttribute("serviceName", serviceName);
			model.addAttribute("serviceName", serviceName);
			model.addAttribute("patient",patient);
			model.addAttribute("hospitalBranchId","0");
		}
		return "pappointmentform";
	}

	@Autowired
	DoctorRepository doctorRepository;
	@RequestMapping("/selectedBranches")
	public String selectedBranches(Model model,Appointment appointment,
			final HttpServletRequest request, HttpSession session,@RequestParam(name = "serviceList", required = false) List<String> serviceList
			,@RequestParam("serviceName") String serviceName){

		String getQry = request.getParameter("getQry");
		String branchId = request.getParameter("branchId");
		System.out.println("_______BRANCHID________"+branchId);
		if (getQry != null && getQry.equals("selectedService") && branchId != null && branchId != "") {
			List<Services> servicelist = serviceRepository.findService(serviceName);//method in repository
			List<Integer> bid = new ArrayList<>();//for storing hospital PKs
			for (Services slist : servicelist) {
				bid.add(slist.getHospitalbranch().getHospitalBranchId());//Adds One by one id to list
			}
			List<HospitalBranch> hblist=new ArrayList<>();//To store rows of matched service branches
			for (Integer serid : bid) {
				//getting hospital by id
				HospitalBranch hospitalBranchById = hospitalBranchService.getHospitalbranchId(serid);
				//adding hospital list by id to list
				hblist.add(hospitalBranchById);
			}
			int pid=appointment.getPatient().getPatientId();
			Patients patient=patientsService.getPatientById(pid);
			List<String> serviceNames = serviceRepository.findDistinctService(serviceList);
			HospitalBranch hospitalBranch=hospitalBranchService.getHospitalbranchId(Integer.parseInt(branchId));
			List<Doctor> doctorNames=doctorRepository.findByDoctorBranchId(branchId,servicelist.get(0).getServiceName());
			
			model.addAttribute("doctorNames",doctorNames);
			model.addAttribute("branchName",hospitalBranch.getHospitalBranchName());
			model.addAttribute("serviceNames",serviceNames);
			model.addAttribute("appointment",appointment);
			model.addAttribute("hblist",hblist);
			model.addAttribute("serviceName",serviceName);
			model.addAttribute("patient",patient);
			model.addAttribute("hospitalBranchId",hospitalBranch.getHospitalBranchId());
		}
		return "pappointmentform";
	}
	
	@RequestMapping("/selectedDoctors")
	public String slected(Model model,Appointment appointment,
			final HttpServletRequest request, HttpSession session,@RequestParam(name = "serviceList", required = false) List<String> serviceList
			,@RequestParam(name="serviceName",required=false) String serviceName,@RequestParam(name="hospitalBranchId", required=false) int branchId) {
		
		String getQry = request.getParameter("getQry");
		String doctorId = request.getParameter("doctorId");
		String hospitalBranchId=request.getParameter("hospitalBranchId");
		
		if (getQry != null && getQry.equals("selectedDoctors") && doctorId != null && doctorId != "") {
			
			Doctor doctor=doctorService.GetDocotorById(Integer.parseInt(doctorId));
			
			List<Services> servicelist = serviceRepository.findService(serviceName);//method in repository
			List<Integer> bid = new ArrayList<>();//for storing hospital PKs
			for (Services slist : servicelist) {
				bid.add(slist.getHospitalbranch().getHospitalBranchId());//Adds One by one id to list
			}
			List<HospitalBranch> hblist=new ArrayList<>();//To store rows of matched service branches
			for (Integer serid : bid) {
				//getting hospital by id
				HospitalBranch hospitalBranchById = hospitalBranchService.getHospitalbranchId(serid);
				//adding hospital list by id to list
				hblist.add(hospitalBranchById);
			}
			int pid=appointment.getPatient().getPatientId();
			Patients patient=patientsService.getPatientById(pid);
			List<String> serviceNames = serviceRepository.findDistinctService(serviceList);
			HospitalBranch hospitalBranch=hospitalBranchService.getHospitalbranchId(branchId);
			String hbid=Integer.toString(branchId);
			List<Doctor> doctorNames=doctorRepository.findByDoctorBranchId(hbid,servicelist.get(0).getServiceName());
			
			model.addAttribute("doctorNames",doctorNames);
			model.addAttribute("branchName",hospitalBranch.getHospitalBranchName());
			model.addAttribute("serviceNames",serviceNames);
			model.addAttribute("appointment",appointment);
			model.addAttribute("hblist",hblist);
			model.addAttribute("serviceName",serviceName);
			model.addAttribute("patient",patient);
			model.addAttribute("doctor",doctor);
		} 
		return "redirect:/Viewdoctors/"+Integer.parseInt(hospitalBranchId)+"/"+appointment.getPatient().getPatientId()+"/"+Integer.parseInt(doctorId);
	}
}
	
