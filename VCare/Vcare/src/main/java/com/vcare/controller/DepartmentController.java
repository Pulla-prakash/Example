package com.vcare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vcare.beans.Department;
import com.vcare.beans.HospitalBranch;
import com.vcare.service.DepartmentService;
import com.vcare.service.HospitalBranchService;

@Controller
@RequestMapping("/")
public class DepartmentController {

	@Autowired

	DepartmentService departmentService;
	
	@Autowired
	
	HospitalBranchService  hospitalBranchService;

	static Logger log = Logger.getLogger(DepartmentController.class.getClass());

	@GetMapping("/depForm")
	public String depPage() {

		return "pprofile";
	}

	@GetMapping("/Dephome")
	public String getAllDepartments(Model model, Department objDepart) {

		List<Department> depList = departmentService.getAllDepartments();
		log.info("list of all departments");

		List<Department> list = new ArrayList<>();
		for (Department dep : depList) {
			if (dep.getIsactive() == 'Y' || dep.getIsactive() == 'y') {
				list.add(dep);
			}
		}
		model.addAttribute("departmentList", list);
		
		model.addAttribute("branch", hospitalBranchService.getAllHospitalbranch());

		model.addAttribute("objName", objDepart.getDepartmentName());

		return "departmentlist";
	}

	@RequestMapping(value = "/addDep", method = RequestMethod.GET)
	public String newEmployee(Model model, @ModelAttribute(value = "departmentObj") Department objDepartment) {
		log.info("department form ");
		model.addAttribute("departmentObj", objDepartment);
		
		Map<Integer, String> BranchMap = dropDownBranch();
		model.addAttribute("BranchMap", BranchMap);
		model.addAttribute("branchId", BranchMap.keySet());
		model.addAttribute("branch", hospitalBranchService.getAllHospitalbranch());

		return "departmentform";
	}

	@RequestMapping(value = "/saveDep", method = RequestMethod.POST)
	public String addDepartment(Model model, @ModelAttribute(value = "departmentObj") Department objDepartment) {
		objDepartment.setIsactive('y');
		log.info("inside deleteDepartment id:::" + objDepartment.getDepartmentId());
		log.info("inside deleteDepartment name:::" + objDepartment.getDepartmentName());
		log.info("inside deleteDepartment name:::" + objDepartment.getDescription());
		model.addAttribute("departmentObj", objDepartment);
		departmentService.updateDepartment(objDepartment);
		List<Department> empList = departmentService.getAllDepartments();
		model.addAttribute("departmentList", empList);
		return "redirect:/Dephome";
	}

	@GetMapping("/editDepartment/{id}")
	public String getById(Model model, @PathVariable("id") int departmentId) {

		Department objSecDepartment = departmentService.getDepartmentById(departmentId);
		log.info("Edit of department in ...");
		log.info("inside getHospitalbranchId id is:::" + departmentService.getDepartmentById(departmentId));
		model.addAttribute("departmentObj", objSecDepartment);
		
		Map<Integer, String> BranchMap = dropDownBranch();
		model.addAttribute("BranchMap", BranchMap);
		model.addAttribute("branchId", BranchMap.keySet());
		model.addAttribute("branch", hospitalBranchService.getAllHospitalbranch());

		return "departmentform";

	}

	@GetMapping("/deleteDepartment/{id}")
	public String deleteService(Model model, @PathVariable int id) {
		log.info("inside deleteHospitalBranch id:::" + id);

		Department inactive = departmentService.getDepartmentById(id);
		inactive.setIsactive('N');
		departmentService.updateDepartment(inactive);
		List<Department> departmentList = departmentService.getAllDepartments();
		model.addAttribute("serviceList", departmentList);

		return "redirect:/Dephome";

	}
	
	
	public Map<Integer,String> dropDownBranch(){
	Map<Integer,String> BranchMap=new HashMap <Integer,String>();
	List<HospitalBranch> branchList=hospitalBranchService.getAllHospitalbranch();
	for(HospitalBranch branchobj :branchList ) {
	BranchMap.put(branchobj.getHospitalBranchId(),branchobj.getHospitalBranchName()); 
	}
	return BranchMap;
	}



}
