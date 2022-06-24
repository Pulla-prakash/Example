package com.vcare.controller;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcare.beans.Doctor;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.Services;
import com.vcare.repository.ServiceRepository;
import com.vcare.service.DoctorService;
import com.vcare.service.HospitalBranchService;
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
	
	
	@GetMapping("/service")
	public String list(Model model) {
		
		
		
		List<Services> serviceList=serviceServices.getAllServices();
	
		model.addAttribute("serviceList", serviceList);
		System.out.println(serviceList.get(0).getServiceName());
		
		
		return "ServiceList";
	}
	
	@RequestMapping(value= "/serPage/{id}",method=RequestMethod.GET)
	public String getAllServices(Model model,@PathVariable("id") int id ,@ModelAttribute(value="serviceList")Services objThServ) {
		
		HospitalBranch objBranch =hospitalBranchService.getHospitalbranchId(id);
		System.out.println("branch Id"+objBranch.getHospitalBranchId());
		model.addAttribute("HospitalId", objBranch.getHospitalBranchId());
		model.addAttribute("HospitalName", objBranch.getHospitalBranchName());
		List<Services> objService =serviceServices.getAllServices();	
		
		model.addAttribute("serviceList", objService);
		return "ServiceList";
	}
	
	@RequestMapping(value="/serForm/{id}",method=RequestMethod.GET)
	public String showForm(Model model,@PathVariable("id") int id,@ModelAttribute(value="serviceList") Services objService) {
		
		HospitalBranch objBranch =hospitalBranchService.getHospitalbranchId(id);
		model.addAttribute("HospitalbranchId", objBranch.getHospitalBranchId());
		System.out.println("name:"+objBranch.getHospitalBranchName());
		model.addAttribute("branch", objBranch.getHospitalBranchName());
		
		model.addAttribute("serviceList", objService);
		
		return"ServiceForm";
	} 
	
	@RequestMapping(value = "/addHospitalBranch/{id}", method = RequestMethod.GET)
	public String saveForm(Model model,@PathVariable("id") int id,Services objService) {
		System.out.println(id);
		HospitalBranch hospBObj=hospitalBranchService.getHospitalbranchId(id);
		model.addAttribute("hospBObj", hospBObj);
		HospitalBranch objBranch =hospitalBranchService.getHospitalbranchId(id);
		model.addAttribute("HospitalId", objBranch.getHospitalBranchId());
		model.addAttribute("name", objBranch.getHospitalBranchName());
		System.out.println("BranchName :"+objBranch.getHospitalBranchName());
		model.addAttribute("serviceList", objService);
		System.out.println("service id:"+objService.getServiceId());
		return"ServiceForm";
	}
	
	@RequestMapping(value = "/saveForm", method = RequestMethod.POST)
	public String addService(Model model,Services objServices) {
		System.out.println(objServices.getServiceId());
		
//		 if(objServices.getServiceId()==0) { 
//			 int Random = (int)(Math.random()*90);
//		 System.out.println("new record:::"+objServices.getServiceId());
//		  objServices.setServiceId(Random); 
//		  serviceServices.addServices(objServices);
//		  }else { serviceServices.UpdateServices(objServices); }
		 serviceServices.addServices(objServices);
	 		//objServices.setCreated(LocalDate.now());
//	 		 System.out.println("new record: set id::"+objEmployee.getEmpId());
//		HospitalBranch hospBObj=hospitalBranchService.getHospitalbranchId(id);
//		model.addAttribute("hospBObj", hospBObj);
	 		System.out.println("hello"+objServices.getServiceId());
//	 		serviceServices.addServices(objServices);
		model.addAttribute("serviceList", objServices);
		return "redirect:/page/service";
	}
	
	
	@GetMapping("/editService/{id}")
	public String getById(Model model, @PathVariable("id") int servicecId, Services objService) {
		Services objSecService = serviceServices.getById(servicecId);
		//objSecService.setUpdated(LocalDate.now());
		System.out.println("inside getHospitalbranchId id is:::" + objSecService.getServiceId());
		model.addAttribute("serviceList", objSecService);
		
		return "ServiceForm";
		

	}
	
	@GetMapping("/deleteService/{id}")
	public String deleteService(Model model, @PathVariable("id") int id) {
		System.out.println("inside deleteHospitalBranch id:::" + id);
		serviceServices.deleteServicesById(id);
		List<Services> serviceList = serviceServices.getAllServices();
		model.addAttribute("serviceList", serviceList);

		return "serviceList";

}
	
//	@GetMapping("/distinctservices")
//	public String distinctServices(Model model,@RequestParam(name="serviceList",required=false) List<String> serviceList,@RequestParam(name="servList",required=false)List<String> servList) {
//		//serviceList= serviceRespository.findDistinctService(servList);
//		List<String> dinthalli=serviceRepository.findDistinctService(serviceList);
//		model.addAttribute("serviceList", dinthalli);
//		System.out.println(dinthalli);
////		model.addAttribute("serviceList", serviceList);
//		return "Index";
//		}
//	
	@GetMapping("/dynamicservices/{id}")
	public String dynamicServices(Model model,@PathVariable String id,@RequestParam(name="serviceList",required=false) List<String> serviceList,Services serviceObj) {
		List<String> serviceNames=serviceRepository.findDistinctService(serviceList);
		for(String s:serviceNames) {
		if(id.equalsIgnoreCase(s)) {
			//creating branches for services
			List <HospitalBranch> hospObj=hospitalBranchService.getAllHospitalbranch();
			
//			model.addAttribute("hospitalBranchList", hospObj);
			model.addAttribute("serviceList", id);
			//Description of service
			List<Services> sameServices=serviceRepository.findService(id);
			model.addAttribute("description",sameServices.get(0).getDescription());
//			sameServices.get(i).getHospitalbranch().getHospitalBranchId();
//			hospObj.get(i).getHospitalBranchId();
			
			System.out.println("DESCRIPTION BLOCK");
			Set<String> dhbname =new HashSet();
			Set<String> bloc=new HashSet();
			for(int i=0;i<hospObj.size();++i) {
				System.out.println("1ST FOR LOOP BLOCK");
				for(int j=0;j<sameServices.size();j++) {
					System.out.println("2ND FOR LOOP BLOCK");
					System.out.println(sameServices.get(j).getServiceName());
					System.out.println(id);
					if(sameServices.get(j).getServiceName().equalsIgnoreCase(id)) {
						String str=sameServices.get(j).getHospitalbranch().getHospitalBranchName();
						
						dhbname.add(str);
						
						String locStr=sameServices.get(j).getHospitalbranch().getHospitalBranchLocation();
					
						dhbname.add(locStr);
						bloc.add(locStr);
		
		
						
						
						System.out.println("IF BLOCK");
					}
				}
			}
			System.out.println(dhbname);
			model.addAttribute("dhbname",dhbname);
			model.addAttribute("bloc", bloc);
			return "dynamicService";
//			for(int i=0;i<=sameServices.size();++i) {
//				
//				
//				if(sameServices.get(i).getHospitalbranch().getHospitalBranchId()==hospObj.get(i).getHospitalBranchId()) {
//					
//					String hbname=hospObj.get(i).getHospitalBranchName();
//					
//					dhbname.add(hbname);
////					model.addAttribute("hospitalBranch", dhbname);
//					
//					System.out.println(hospObj.get(i).getHospitalBranchName());
//					model.addAttribute("hospitalBranchList", hospObj);
//				}
//			}
//			model.addAttribute("hospitalBranch", dhbname);
//			hospObj.get(0).getHospitals().getHospitalBranch();
//			return "dynamicService";
		}
		
		}
		
		return "redirect:/vcare";
		}

	@GetMapping("/indexdoctors")
	public String indexDoctors(Model model) {
		List<Doctor> docList=doctorService.getAllDoctors();
		System.out.println(docList);
		System.out.println("hoo ");
		
		model.addAttribute("docList", docList);
		return "redirect:/vcare";
	}
	
	}
	
//	@GetMapping("/specificbranch/{str}")
//	public String specificBranch(Model model,@PathVariable("str") String servname,List<String> serviceList){
//		
//		List<String> serviceNames=serviceRepository.findDistinctService(serviceList);
//		for(String s:serviceNames) {
//			if(servname.equalsIgnoreCase(s)) {
//				//creating branches for services
//				List <HospitalBranch> hospObj=hospitalBranchService.getAllHospitalbranch();
//				model.addAttribute("hospitalBranchList", hospObj);
//				model.addAttribute("serviceList", servname);
//				return "dynamicService";
//			}
//			
//			
//			}
//				
//		List<String> servicebranch=serviceRepository.findbranch(servname);
//		model.addAttribute("servicebranch", servicebranch);
//		return "redirect:/vcare";
//	}


















