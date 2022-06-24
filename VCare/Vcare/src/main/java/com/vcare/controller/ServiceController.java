package com.vcare.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vcare.beans.Doctor;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.News;
import com.vcare.beans.Rating;
import com.vcare.beans.Services;
import com.vcare.repository.DoctorRatingRepository;
import com.vcare.repository.ServiceRepository;
import com.vcare.service.DoctorRatingService;
import com.vcare.service.DoctorService;
import com.vcare.service.HospitalBranchService;
import com.vcare.service.HospitalService;
import com.vcare.service.NewsService;
import com.vcare.service.ServiceService;

@Controller
@RequestMapping("/page")
public class ServiceController {

	@Autowired

	ServiceService serviceServices;

	@Autowired

	HospitalBranchService hospitalBranchService;
	@Autowired
	ServiceRepository serviceRepository;

	@Autowired
	DoctorService doctorService;
	@Autowired
	HospitalService hospitalService;

	@Autowired
	DoctorRatingRepository doctorRatingRepository;
	@Autowired
	DoctorRatingService doctorRatingService;

	static Logger log = Logger.getLogger(ServiceController.class.getClass());

	@GetMapping("/service")
	public String list(Model model) {
	List<Services> serviceList = serviceServices.getAllServices();
	model.addAttribute("serviceList", serviceList);
	for (Services service : serviceList) {
//	log.info("HospitalBranchName::" + service.getHospitalbranch().getHospitalBranchName());
//	log.info("HospitalBranchAddress::" + service.getHospitalbranch().getHospitalBranchAddress());
//	log.info("HospitalBranchNumber::" + service.getHospitalbranch().getHospitalBranchNumber());
	}
	return "servicelist";
	}



	@RequestMapping(value = "/serPage/{id}", method = RequestMethod.GET)
	public String getAllServices(Model model, @PathVariable("id") int id,
			@ModelAttribute(value = "serviceList") Services objThServ) {

		List<Services> objService = serviceServices.getAllServices();

		List<Services> list = new ArrayList<>();
		for (Services serve : objService) {
			if (serve.getIsActive() == 'Y' || serve.getIsActive() == 'y') {
				list.add(serve);
			}
		}

		model.addAttribute("serviceList", list);
		return "servicelist";
	}

	@RequestMapping(value = "/serForm/{id}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable("id") int id,
			@ModelAttribute(value = "serviceList") Services objService) {

		HospitalBranch objBranch = hospitalBranchService.getHospitalbranchId(id);
		model.addAttribute("HospitalbranchId", objBranch.getHospitalBranchId());
		log.info("name:::" + objBranch.getHospitalBranchName());
		model.addAttribute("branch", objBranch.getHospitalBranchName());

		model.addAttribute("serviceObj", objBranch);

		return "serviceform";
	}

	@RequestMapping(value = "/addHospitalBranch/{id}", method = RequestMethod.GET)
	public String saveForm(Model model, @PathVariable("id") int id, Services objService) {
		try {
			model.addAttribute("serviceObj", objService);
			log.info("service id::" + objService.getServiceId());
			log.info("Branch id::" + id);
			HospitalBranch hospBObj = hospitalBranchService.getHospitalbranchId(id);
			model.addAttribute("hospBObj", hospBObj);
			HospitalBranch objBranch = hospitalBranchService.getHospitalbranchId(id);
			model.addAttribute("HospitalId", hospBObj.getHospitalBranchId());
			model.addAttribute("name", objBranch.getHospitalBranchName());
			log.info("BranchName ::" + objBranch.getHospitalBranchName());

			return "serviceform";
		} catch (Exception ex) {

		}
		log.info("service id::" + objService.getServiceId());
		model.addAttribute("serviceObj", objService);
		return "serviceform";
	}

	@RequestMapping(value = "/addHospital/{id}", method = RequestMethod.GET)

	public String Hospital(Model model, @PathVariable("id") int id, Services objServices) {

		return "serviceform";

	}

	@RequestMapping(value = "/saveForm", method = RequestMethod.POST)
	public String addService(Model model, Services objServices) {
		log.info("Service id:::" + objServices.getServiceId());
		objServices.setIsActive('Y');
		serviceServices.UpdateServices(objServices);
		log.info("hello:::" + objServices.getServiceId());
		return "redirect:/page/service";
	}

	@GetMapping("/editService/{id}")
	public String getById(Model model, @PathVariable("id") int servicecId) {
		Services objSecService = serviceServices.getById(servicecId);
		log.info("inside getHospitalbranchId id is:::" + objSecService.getServiceId());
		model.addAttribute("serviceObj", objSecService);
		model.addAttribute("HospitalId", objSecService.getHospitalbranch().getHospitalBranchId());
		log.info("inside editHospitalBranch id:::" + objSecService.getHospitalbranch().getHospitalBranchId());
		return "serviceform";

	}

	@GetMapping("/deleteService/{id}")
	public String deleteService(Model model, @PathVariable("id") int id) {
		log.info("inside deleteHospitalBranch id:::" + id);

		Services inactive = serviceServices.getById(id);
		inactive.setIsActive('N');
		serviceServices.UpdateServices(inactive);
		List<Services> serviceList = serviceServices.getAllServices();
		model.addAttribute("serviceList", serviceList);

		return "redirect:/page/service";

	}
	@Autowired
	ServiceService Serviceservice;

	@GetMapping("/dynamicservices/{id}")
	public String dynamicServices(Model model, @PathVariable String id,
			@RequestParam(name = "serviceList", required = false) List<String> serviceList, Services serviceObj) {
		List<String> serviceNames = serviceRepository.findDistinctService(serviceList);
		for (String s : serviceNames) {
			if (id.equalsIgnoreCase(s)) {
				// creating branches for services
				List<HospitalBranch> hospObj = hospitalBranchService.getAllHospitalbranch();

				// model.addAttribute("hospitalBranchList", hospObj);
				model.addAttribute("serviceList", id);
				// Description of service
				List<Services> sameServices = serviceRepository.findService(id);
				model.addAttribute("description", sameServices.get(0).getDescription());

				log.info("DESCRIPTION BLOCK");
				Set<String> dhbname = new HashSet();
				Set<String> bloc = new HashSet();
				for (int i = 0; i < hospObj.size(); ++i) {
					log.info("1ST FOR LOOP BLOCK");
					for (int j = 0; j < sameServices.size(); j++) {
						log.info("2ND FOR LOOP BLOCK");
						log.info(sameServices.get(j).getServiceName());
						log.info(sameServices.get(j).hospitalbranch.getHospitalBranchName());
						log.info(id);
						if (sameServices.get(j).getServiceName().equalsIgnoreCase(id)) {
							String str = sameServices.get(j).getHospitalbranch().getHospitalBranchName();

							dhbname.add(str);

							log.info("IF BLOCK");
						}
					}
				}
				log.info(dhbname);
				model.addAttribute("dhbname", dhbname);
				model.addAttribute("bloc", bloc);
				return "dynamicservice";
			}

		}

		return "redirect:/vcare";
	}

	@GetMapping("/indexdoctors/{id}")
	public String indexDoctors(Model model, @PathVariable("id") int id) {
		List<Doctor> doctorList = doctorService.getAllDoctor();
		System.out.println(doctorList);
		System.out.println("hoo ");

		model.addAttribute("doctorList", doctorList);

		Doctor doc = doctorService.GetDocotorById(id);
		System.out.println(doc.getDoctorId());

		if (id == doc.getDoctorId()) {
			model.addAttribute("doc", doctorService.GetDocotorById(id));
			System.out.println(doc);
		}
		double average;
		long totalrating;
		// Getting Average rating for doctor
		if (doctorRatingRepository.sumOfRating(doc.getDoctorId()) == null) {
			totalrating = 0;
			average = 0.0;
			model.addAttribute("average", average);
		} else {
			totalrating = doctorRatingRepository.sumOfRating(doc.getDoctorId());// Sum of ratings

			List<Rating> pcount = doctorRatingService.GetAllDoctorRating();// totol entries
			long count = pcount.size(); // no.of patinets given rating
			average = (float) totalrating / count;
			model.addAttribute("average", average);
		}
		// getting no.of persons according to rating
		Long fivecount = doctorRatingRepository.fivecount(doc.getDoctorId());
		model.addAttribute("fivecount", fivecount);
		Long fourcount = doctorRatingRepository.fourcount(doc.getDoctorId());
		model.addAttribute("fourcount", fourcount);
		Long threecount = doctorRatingRepository.threecount(doc.getDoctorId());
		model.addAttribute("threecount", threecount);
		Long twocount = doctorRatingRepository.twocount(doc.getDoctorId());
		model.addAttribute("twocount", twocount);
		Long onecount = doctorRatingRepository.onecount(doc.getDoctorId());
		model.addAttribute("onecount", onecount);
		return "doctorscreen";
	}

	@GetMapping("/indexdoctorscreen/{id}")
	public String indexDoctorScreen(Model model, @PathVariable("id") int id) {
		Doctor doc = doctorService.GetDocotorById(id);
		if (id == doc.getDoctorId()) {
			model.addAttribute("doc", doctorService.GetDocotorById(id));
			System.out.println(doc);
		}
		List<Rating> ratingList = doctorRatingRepository.docrating(id);
		model.addAttribute("rating", ratingList);
		return "doctorscreen";
	}

	@GetMapping(value = "/getSubCatagory", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Services> getSubCatagory(@RequestParam Integer hospitalBranchId) {

		List<Services> list = serviceServices.findSubCatagoryByCatagory(hospitalBranchId);

		System.out.println("catagoryId " + hospitalBranchId);

		return list;
	}
	@Autowired
	NewsService newsService;
	@GetMapping("/servicehospitalPage/{id}")
	public String hospitalPage(Model model,@PathVariable("id") int HospitalbranchId){
	List<HospitalBranch> hospBranchList = hospitalBranchService.getAllHospitalbranch();
	model.addAttribute("hospitalBranchList", hospBranchList);
	List<News> newslists = newsService.getLatestNews();
	model.addAttribute("newsLists", newslists);
	HospitalBranch objHospitalBranch = hospitalBranchService.getHospitalbranchId(HospitalbranchId);
	model.addAttribute("objHospitalBranch", objHospitalBranch); 
	return "hospitalpage";
	}

}
