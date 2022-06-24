package com.vcare.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vcare.beans.Employees;

public interface EmployeesService {

	List<Employees> getAllEmployees();

	Employees addEmployees(Employees employees);

	void UpdateEmployees(Employees employees);

	void deleteEmployeesById(int employeeId);

	Employees getById(int employeeId);
	
	String validateduplicate(String email);
	
	Employees getEmployee(String email, String password);
	
	void saveEmployees(MultipartFile file,Employees employees);

	



}
