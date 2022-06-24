package com.vcare.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vcare.beans.Admin;
import com.vcare.beans.Insurance;
import com.vcare.service.AdminService;
import com.vcare.service.InsuranceService;

@Controller
public class InsuranceController<ModelAndView> {

	@Autowired
	InsuranceService insuranceService;
	@Autowired
	private AdminService adminService;
	
	static Logger log=Logger.getLogger(InsuranceController.class.getClass());

	@RequestMapping(value = "addInsurance/{id}", method = RequestMethod.GET)
	public String newInsurance(Model model, @ModelAttribute(value = "insurance") Insurance insurance,
			@PathVariable int id) {

		Admin adminobj = adminService.getadminById(id);
		int a = adminobj.getAdminId();
		String name = adminobj.getName();
		model.addAttribute("admin", a);
		model.addAttribute("adminname", name);

		model.addAttribute("insurance", insurance);
		return "insuranceform";
	}

	@PostMapping("/saveIns/{id}")
	public String saveProduct(Model model, @RequestParam("file") MultipartFile file,

			Insurance insurance, @PathVariable int id) throws IOException {

		Admin adminobj = adminService.getadminById(id);

		model.addAttribute("admin", adminobj);

		insurance.setCreatedBy(adminobj.getAdminId());
		// insuranceService.addInsurance(insurance);
		insurance.setIsactive('Y');
		insurance.setInsurencepic(Base64.getEncoder().encodeToString(file.getBytes()));
		
//			  insuranceService.saveProduct(file, insuranceName, insuranceDescription,createdBy,updateBy);
		insuranceService.saveInsurance(insurance);
		log.info(insurance.getInsuranceName());
		return "redirect:/insurancelist/{id}";
	}

	@RequestMapping("/insurancelist/{id}")
	public String insuranceList(Model model, Insurance insurance, @PathVariable int id) {

		Admin adminobj = adminService.getadminById(id);
		model.addAttribute("admin", adminobj);

		List<Insurance> list = insuranceService.getAllInsurance();
		model.addAttribute("insurancelist", list);
		return "insurancelist";
	}

	@RequestMapping("deleteInsurance/{id}/{delid}")
	public String deleteinsurance(Model model, @PathVariable int delid, @PathVariable int id, Insurance insurance) {
		Admin adminobj = adminService.getadminById(id);
		model.addAttribute("admin", adminobj);
		insuranceService.deleteInsuranceById(delid);
		List<Insurance> list = insuranceService.getAllInsurance();
		model.addAttribute("insurancelist", list);
		return "redirect:/insurancelist/{id}";
	}

	// Fetch single emp
	@GetMapping("/editIns/{id}/{insid}")
	public String getinsuranceById(Model model, @PathVariable("id") int Id, @PathVariable("insid") int insid) {
		Insurance insurance = insuranceService.getInsuranceById(insid);
		model.addAttribute("insurance", insurance);
		model.addAttribute("insuranceId", Id);
		Admin adminobj = adminService.getadminById(Id);
		model.addAttribute("admin", adminobj);
		int a = adminobj.getAdminId();
		String name = adminobj.getName();
		// insuranceService.saveProduct(file, insurance);
		model.addAttribute("admin", a);
		model.addAttribute("adminname", name);
		log.info("inside getinsuranceById id is:::" + insurance.getInsuranceId());

		return "insuranceform";
	}

	@GetMapping("/insurance/{id}")
	public String viewinsurance(Model model, @PathVariable("id") int Id) {
		Insurance insurance = insuranceService.getInsuranceById(Id);

		model.addAttribute("insurancename", insurance.getInsuranceName());
		model.addAttribute("description", insurance.getInsuranceDescription());
		model.addAttribute("image", insurance.getInsurencepic());
		model.addAttribute("termscondition", insurance.getTermsconditions());

		return "insurancescreens";
	}

}
