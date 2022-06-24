package com.vcare.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.vcare.beans.Aboutus;
import com.vcare.beans.Admin;
import com.vcare.beans.Appointment;
import com.vcare.beans.Doctor;
import com.vcare.beans.Hospital;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.Insurance;
import com.vcare.beans.News;
import com.vcare.beans.Patients;
import com.vcare.beans.Services;
import com.vcare.beans.SocialNetworks;
import com.vcare.repository.DoctorRepository;
import com.vcare.repository.PatientsRepository;
import com.vcare.repository.ServiceRepository;
import com.vcare.service.AboutusService;
import com.vcare.service.AdminService;
import com.vcare.service.DoctorService;
import com.vcare.service.HospitalBranchService;
import com.vcare.service.HospitalService;
import com.vcare.service.InsuranceService;
import com.vcare.service.NewsService;
import com.vcare.service.PatientsService;
import com.vcare.service.SocialNetworkService;
import com.vcare.utils.VcareUtilies;

@Controller
@RequestMapping("/")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private NewsService newsservice;

	@Autowired
	ServiceRepository serviceRespository;

	@Autowired
	DoctorService doctorService;

	@Autowired
	InsuranceService insuranceService;

	@Autowired
	HospitalService hospitalService;

	@Autowired

	AboutusService aboutusService;

	@Autowired

	SocialNetworkService socialNetworkService;
	
	@Autowired
	
	HospitalBranchService  hospitalBranchService;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	PatientsRepository  patientsRepository;
	
	@Autowired
	PatientsService patientsService;

	static Logger log = Logger.getLogger(AdminController.class.getName());

	@GetMapping("/")
	public String viewHomePages(Model model, News news,
			@RequestParam(name = "serviceList", required = false) List<String> serviceList,
			@RequestParam(name = "servList", required = false) List<String> servList,final HttpServletRequest request,
			final HttpServletResponse response,HttpSession session) {

		List<News> newslists = newsservice.getLatestNews();
		model.addAttribute("newsLists", newslists);
		List<String> serviceNames = serviceRespository.findDistinctService(serviceList);
		model.addAttribute("serviceList", serviceNames);

		log.info("Hello this is a debug message");
		log.info("Hello this is an info message");
		log.warn("++++++++++");
		List<Doctor> docList = doctorService.getAllDoctor();
		model.addAttribute("docList", docList);
		log.info("===========INDEX==============");
		log.info("NEWS :" + newslists);
		log.info("DOCTORS :" + docList);
		log.info("SERVICES :" + serviceNames);

		List<Aboutus> listAbout = aboutusService.getAllAbouts();

		model.addAttribute("aboutlist", listAbout);

		log.info("about:::::" + listAbout);
		
		
		List<HospitalBranch> hospBranchList = hospitalBranchService.getAllHospitalbranch();
		System.out.println("inside getAllHospitals this will get the all hospitalsBranch:::");
		model.addAttribute("hospitalBranchList", hospBranchList);
		StringBuffer obj = new StringBuffer();
		int i=1;
		for (HospitalBranch a : hospBranchList) { obj.append("[\"" +a.getHospitalBranchName()+ "\"," + a.getLatitude() + "," + a.getLongitude() + "," + i + "],");
		i++;
		}
		model.addAttribute("branches", obj.toString());



		List<SocialNetworks> networkList = socialNetworkService.getAllNetworks();

		model.addAttribute("networkList", networkList);
		try {
		List<News> newslist = newslists.subList(0, 2);
		System.out.println("fdgyuhjkvgchfyuikjhgfyuil"+newslist);
		model.addAttribute("newsList", newslist);
		}catch(IndexOutOfBoundsException e) {
		System.out.println("vgqedvgdhvedhvEYUDGHebdhjB4534567456");
		model.addAttribute("newsList", null);
		}



		List<Hospital> hospList = hospitalService.getAllHospitals();
		model.addAttribute("hospitalsList", hospList);

		List<Insurance> list = insuranceService.getAllInsurance();
		model.addAttribute("insurancelist", list);

		log.info("Hospital:::::" + hospList);
		final StringBuffer url1 = request.getRequestURL();
		HttpSession session1 = request.getSession();
		session1.setAttribute("url", url1);
		
		return "index";
	}

	@PostMapping("/saveAdmin")
	public String saveAdmin(Model model, @ModelAttribute("admin") Admin admin) {

		log.info("ADMIN : " + admin);
		log.info("Registered");

		String strEncPassword = VcareUtilies.getEncryptSecurePassword(admin.getPassword(), "vcare");
		admin.setPassword(strEncPassword);
		adminService.saveAdmin(admin);
		// model.addAttribute("name",admin.getName());
		return "index";

	}

	@GetMapping("/checkCredentials")
	public String checkCredentials(Model model, Admin admin) {

		if (!admin.getCaptchaAdmin().equals(admin.getUserCaptcha())) {
			model.addAttribute("message", "Please enter valid Captcha");
			return "index";
		}

		String str = VcareUtilies.getEncryptSecurePassword(admin.getPassword(), "vcare");
		Admin adminOne = adminService.getAdmin(admin.getEmail(), str);
		model.addAttribute("admin", adminOne);
		log.trace("LOGIN CREDENTIALS " + adminOne);
		if (adminOne != null) {

			log.info(adminOne.getName() + " Successfully login");
			return "adminex";
		} else {

			log.error("invalid credentials");
			return "index";
		}
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView UserforgotPasswordPage(Admin admin) {

		log.info("hello:::::" + admin.getEmail());
		System.out.println("entered into user/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("forgotPassword");
		mav.addObject("admin", admin);
		return mav;
	}

	@PostMapping(value = "/validateEmailId")
	public String checkMailId(Model model, Admin admin) {
		System.out.println("entered into user/controller::::check EmailId existing or not");
		System.out.println("UI given mail Id:" + admin.getEmail());
		System.out.println("UI given mail Id:" + admin.getName());
		Admin adminObj = adminService.findByMail(admin.getEmail());
		if (adminObj != null) {
			String s1 = "";
			model.addAttribute("message", s1);
			System.out.println("UI given mail Id:" + adminObj.getEmail());
			model.addAttribute("admin", admin);
			return "resetPassword";
		} else {
			System.out.println("Invalid Mail");
			String s1 = "Email-Id Not Exists";
			model.addAttribute("message", s1);
			model.addAttribute("admin", new Admin());
			return "forgotPassword";
		}
	}

	@RequestMapping(value = "/updateClientPassword", method = RequestMethod.POST)
	public ModelAndView updateUserPassword(Model model, @ModelAttribute("admin") Admin adminObj) {
		Admin objAdmin = adminService.findByMail(adminObj.getEmail());
		System.out.println("inside updateClientPassword after update id is:::" + adminObj.getAdminId());
		String strEncPassword = VcareUtilies.getEncryptSecurePassword(adminObj.getPassword(), "vcare");
		adminObj.setPassword(strEncPassword);
		objAdmin.setPassword(adminObj.getPassword());
		System.out.println(adminObj.getPassword());

		System.out.println("in update method pUser:: name " + objAdmin.getName());
		adminService.UpdateAdmin(objAdmin);
		// pUser.setUpdatedDate(LocalDateTime.now());
		System.out.println("password is updated sucessfully");
		ModelAndView mav = new ModelAndView("index");
		System.out.println("login page is displayed");
		model.addAttribute("admin", objAdmin);
		return mav;
	}
	
	/*
	 * @RequestMapping("/checkService") public String selectedService(Model
	 * model,Appointment appointment, final HttpServletRequest request, HttpSession
	 * session,@RequestParam(name = "serviceList", required = false) List<String>
	 * serviceList) {
	 * 
	 * String getQry = request.getParameter("getQry"); String serviceName =
	 * request.getParameter("serviceName"); System.out.println(getQry + "++++++++" +
	 * serviceName); if (getQry != null && getQry.equals("selectedService") &&
	 * serviceName != null && serviceName != "") { List<Services> servicelist =
	 * serviceRepository.findService(serviceName);//method in repository
	 * List<Integer> bid = new ArrayList<>();//for storing hospital PKs for
	 * (Services slist : servicelist) {
	 * bid.add(slist.getHospitalbranch().getHospitalBranchId());//Adds One by one id
	 * to list } List<HospitalBranch> hblist=new ArrayList<>();//To store rows of
	 * matched service branches for (Integer serid : bid) { //getting hospital by id
	 * HospitalBranch hospitalBranchById =
	 * hospitalBranchService.getHospitalbranchId(serid); //adding hospital list by
	 * id to list hblist.add(hospitalBranchById); } int
	 * pid=appointment.getPatient().getPatientId(); Patients
	 * patient=patientsService.getPatientById(pid); List<String> serviceNames =
	 * serviceRepository.findDistinctService(serviceList);
	 * model.addAttribute("serviceNames",serviceNames);
	 * model.addAttribute("appointment",appointment); if(hblist.size()!=0) {
	 * model.addAttribute("hblist",hblist); } else {
	 * model.addAttribute("hblist",""); } // session.setAttribute("serviceName",
	 * serviceName); model.addAttribute("serviceName", serviceName);
	 * model.addAttribute("patient",patient);
	 * model.addAttribute("hospitalBranchId","0"); } return "checkappointment"; }
	 * 
	 * @Autowired DoctorRepository doctorRepository;
	 * 
	 * @RequestMapping("/checkBranches") public String selectedBranches(Model
	 * model,Appointment appointment, final HttpServletRequest request, HttpSession
	 * session,@RequestParam(name = "serviceList", required = false) List<String>
	 * serviceList ,@RequestParam("serviceName") String serviceName){
	 * 
	 * String getQry = request.getParameter("getQry"); String branchId =
	 * request.getParameter("branchId");
	 * System.out.println("_______BRANCHID________"+branchId); if (getQry != null &&
	 * getQry.equals("selectedService") && branchId != null && branchId != "") {
	 * List<Services> servicelist =
	 * serviceRepository.findService(serviceName);//method in repository
	 * List<Integer> bid = new ArrayList<>();//for storing hospital PKs for
	 * (Services slist : servicelist) {
	 * bid.add(slist.getHospitalbranch().getHospitalBranchId());//Adds One by one id
	 * to list } List<HospitalBranch> hblist=new ArrayList<>();//To store rows of
	 * matched service branches for (Integer serid : bid) { //getting hospital by id
	 * HospitalBranch hospitalBranchById =
	 * hospitalBranchService.getHospitalbranchId(serid); //adding hospital list by
	 * id to list hblist.add(hospitalBranchById); } int
	 * pid=appointment.getPatient().getPatientId(); Patients
	 * patient=patientsService.getPatientById(pid); List<String> serviceNames =
	 * serviceRepository.findDistinctService(serviceList); HospitalBranch
	 * hospitalBranch=hospitalBranchService.getHospitalbranchId(Integer.parseInt(
	 * branchId)); List<Doctor>
	 * doctorNames=doctorRepository.findByDoctorBranchId(branchId,servicelist.get(0)
	 * .getServiceName());
	 * 
	 * model.addAttribute("doctorNames",doctorNames);
	 * model.addAttribute("branchName",hospitalBranch.getHospitalBranchName());
	 * model.addAttribute("serviceNames",serviceNames);
	 * model.addAttribute("appointment",appointment);
	 * model.addAttribute("hblist",hblist);
	 * model.addAttribute("serviceName",serviceName);
	 * model.addAttribute("patient",patient);
	 * model.addAttribute("hospitalBranchId",hospitalBranch.getHospitalBranchId());
	 * } return "checkappointment"; }
	 * 
	 * @RequestMapping("/checkDoctors") public String slected(Model
	 * model,Appointment appointment, final HttpServletRequest request, HttpSession
	 * session,@RequestParam(name = "serviceList", required = false) List<String>
	 * serviceList ,@RequestParam(name="serviceName",required=false) String
	 * serviceName,@RequestParam(name="hospitalBranchId", required=false) int
	 * branchId) {
	 * 
	 * String getQry = request.getParameter("getQry"); String doctorId =
	 * request.getParameter("doctorId"); String
	 * hospitalBranchId=request.getParameter("hospitalBranchId");
	 * 
	 * if (getQry != null && getQry.equals("selectedDoctors") && doctorId != null &&
	 * doctorId != "") {
	 * 
	 * Doctor doctor=doctorService.GetDocotorById(Integer.parseInt(doctorId));
	 * 
	 * List<Services> servicelist =
	 * serviceRepository.findService(serviceName);//method in repository
	 * List<Integer> bid = new ArrayList<>();//for storing hospital PKs for
	 * (Services slist : servicelist) {
	 * bid.add(slist.getHospitalbranch().getHospitalBranchId());//Adds One by one id
	 * to list } List<HospitalBranch> hblist=new ArrayList<>();//To store rows of
	 * matched service branches for (Integer serid : bid) { //getting hospital by id
	 * HospitalBranch hospitalBranchById =
	 * hospitalBranchService.getHospitalbranchId(serid); //adding hospital list by
	 * id to list hblist.add(hospitalBranchById); } int
	 * pid=appointment.getPatient().getPatientId(); Patients
	 * patient=patientsService.getPatientById(pid); List<String> serviceNames =
	 * serviceRepository.findDistinctService(serviceList); HospitalBranch
	 * hospitalBranch=hospitalBranchService.getHospitalbranchId(branchId); String
	 * hbid=Integer.toString(branchId); List<Doctor>
	 * doctorNames=doctorRepository.findByDoctorBranchId(hbid,servicelist.get(0).
	 * getServiceName());
	 * 
	 * model.addAttribute("doctorNames",doctorNames);
	 * model.addAttribute("branchName",hospitalBranch.getHospitalBranchName());
	 * model.addAttribute("serviceNames",serviceNames);
	 * model.addAttribute("appointment",appointment);
	 * model.addAttribute("hblist",hblist);
	 * model.addAttribute("serviceName",serviceName);
	 * model.addAttribute("patient",patient); model.addAttribute("doctor",doctor); }
	 * return
	 * "redirect:/Viewdoctors/"+Integer.parseInt(hospitalBranchId)+"/"+appointment.
	 * getPatient().getPatientId()+"/"+Integer.parseInt(doctorId); }
	 */
	
	
	@RequestMapping("/checkService")
	public String selectedService(Model model,HospitalBranch hospitalBranch) {
		
		
        
		List<HospitalBranch> branchs=hospitalBranchService.getAllHospitalbranch();
		model.addAttribute("branchs", branchs);
		Doctor  doctor=new Doctor();
		return "checkappointment";
	}
	
	
}

	


