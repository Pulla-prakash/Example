package com.vcare.controller;

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
import com.vcare.beans.Doctor;
import com.vcare.beans.Hospital;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.Insurance;
import com.vcare.beans.News;
import com.vcare.beans.SocialNetworks;
import com.vcare.repository.ServiceRepository;
import com.vcare.service.AboutusService;
import com.vcare.service.AdminService;
import com.vcare.service.DoctorService;
import com.vcare.service.HospitalBranchService;
import com.vcare.service.HospitalService;
import com.vcare.service.InsuranceService;
import com.vcare.service.NewsService;
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
	
	

}
