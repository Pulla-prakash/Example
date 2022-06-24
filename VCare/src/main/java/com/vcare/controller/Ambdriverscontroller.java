package com.vcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.vcare.beans.Ambulance;
import com.vcare.beans.AmbulanceDriverAssosiation;
import com.vcare.beans.Department;
import com.vcare.beans.Employees;
import com.vcare.beans.HospitalBranch;
import com.vcare.repository.Ambdriversrepository;

import com.vcare.repository.AmbulanceRepository;
import com.vcare.repository.DepartmentRepository;
import com.vcare.repository.EmployeesRepository;
import com.vcare.service.AmbulanceDriverService;
import com.vcare.service.AmbulanceService;
import com.vcare.service.DepartmentService;
import com.vcare.service.EmployeesService;
import com.vcare.service.HospitalBranchService;
@Controller
public class Ambdriverscontroller {

	@Autowired
	AmbulanceDriverService ambulanceDriverService;
	@Autowired
	AmbulanceService ambulanceservice;
	@Autowired
	EmployeesService employeeService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	Ambdriversrepository ambdriversrepository;
	
	@Autowired
	HospitalBranchService hospitalBranchService;
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	EmployeesRepository employeesRepository;
	@Autowired
	AmbulanceRepository ambulancerepo;
	
	@GetMapping("/AmbulanceDriverList")
	public String getAllAmbdriver(Model model) {

//	List<Ambulance> AmbList= ambulanceservice.getAllAmbulance();
//		model.addAttribute("ambulanceList", AmbList);
		
		List<AmbulanceDriverAssosiation> ambdriverlist= ambulanceDriverService.getAllAambulancedriver();
		
		model.addAttribute("ambdriverlist", ambdriverlist);
		
		return "ambulancedriverlist";
	}

	@GetMapping("/AddAmbdriver/{HbranchId}")
	public String showNewdoctorsForm(Model model, AmbulanceDriverAssosiation ambulancedriverobj,@PathVariable("HbranchId") int HbranchId,HospitalBranch hospitalBranch) {
		HospitalBranch hospitalbranch= hospitalBranchService.getHospitalbranchId(HbranchId);
		System.out.println("fhjefbwegjhw"+HbranchId);
		model.addAttribute("HbranchId",hospitalbranch.getHospitalBranchId());
		System.out.println("branchid::::::::::::::::"+hospitalBranch.getHospitalBranchEmailId());
		System.out.println("this method is implemented");
		List<Ambulance> AmbList= ambulancerepo.getambbyBranchList(HbranchId);
		model.addAttribute("ambulanceList", AmbList);
		  Department deptObj = departmentRepository.dept(HbranchId);
		  System.out.println(deptObj.getHospitalBranch().getHospitalBranchId());
		  
		  List<Employees> empdriverlist =
		 employeesRepository.empDriverList(deptObj.getDepartmentId());
		  model.addAttribute("empdriverlist", empdriverlist);
		return "ambdriverform";
	}
	
	
	@PostMapping("/saveAmbdriver")
	public String saveAmbDriver(Model model,AmbulanceDriverAssosiation ambulancedriverobj,HospitalBranch hospitalBranch){
		System.out.println("helooooooo");
		String objambdriver=ambulanceDriverService.validateduplicate(ambulancedriverobj.getAmbulancetype());
		ambulancedriverobj.setIsactive('y');
		
		System.out.println("save method2");
		
		ambulanceDriverService.addAmbulancedriver(ambulancedriverobj);
		return  "redirect:/AmbulanceDriverList";
		
		//List<AmbulanceDriverAssosiation> ambdriverlist= ambulanceDriverService.getAllAambulancedriver();
//		 if (objambdriver==null) {
//			 
//			 ambulancedriverobj.setIsactive('y');
//			
//				System.out.println("save method2");
//				
//				ambulanceDriverService.addAmbulancedriver(ambulancedriverobj);
//				return  "redirect:/AmbulanceDriverList";
//			
//		}else {
//			int a=hospitalBranch.getHospitalBranchId();
//				System.out.println("this is return statement");
//				model.addAttribute("msg","driver already assigned to this ambulance ");
//			return"ambdriverform";
//			
//		}
		}


	@GetMapping("/deleteAmbdriver/{ambdriversId}")
	public String deleteHospitalBranch(Model model, @PathVariable("ambdriversId") int ambdriversId) {
		System.out.println("inside deleteHospitalBranch id:::" + ambdriversId);

		AmbulanceDriverAssosiation inactive = ambulanceDriverService.getAmbdriverId(ambdriversId);
		inactive.setIsactive('N');
		
		ambulanceDriverService.addAmbulancedriver(inactive);
		return "redirect:/AmbulanceDriverList";
	}

	@GetMapping("/editAmbulancedriver/{ambdriversId}")
	public String getById(Model model, @PathVariable("ambdriversId") int ambdriversId) {

		AmbulanceDriverAssosiation ambulancedriverob = ambulanceDriverService.getById(ambdriversId);
		model.addAttribute("ambulancedriverobj", ambulancedriverob);

		System.out.println("inside getHospitalbranchId id is:::" + ambulanceservice.getById(ambdriversId));
		ambulanceDriverService.addAmbulancedriver(ambulancedriverob);
		return "ambdriverform";
	}
	@GetMapping("/selectambulancedriver/{hbranchId}")
	public String getbyid(Model model,AmbulanceDriverAssosiation ambulancedriverob, @PathVariable("hbranchId") String hbranchId) {
		List<AmbulanceDriverAssosiation> AmbdriverList = ambdriversrepository.ambdriverList(Integer.parseInt(hbranchId));
		model.addAttribute("ambdriverlist", AmbdriverList);
		return "ambulanceselection";
		
}

  
  
 
	
	
	
}
