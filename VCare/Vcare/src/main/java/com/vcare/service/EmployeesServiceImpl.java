package com.vcare.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vcare.beans.Employees;

import com.vcare.repository.EmployeesRepository;

@Service
public class EmployeesServiceImpl implements EmployeesService {

	@Autowired

	EmployeesRepository employeesRepository;

	@Override
	public List<Employees> getAllEmployees() {
		// TODO Auto-generated method stub
		return employeesRepository.findAll();
	}

	@Override
	public Employees addEmployees(Employees employees) {
		// TODO Auto-generated method stub
		return employeesRepository.save(employees);
	}

	@Override
	public void UpdateEmployees(Employees employees) {
		// TODO Auto-generated method stub
		employeesRepository.save(employees);
	}

	@Override
	public void deleteEmployeesById(int employeeId) {
		// TODO Auto-generated method stub
		employeesRepository.deleteById(employeeId);
	}

	@Override
	public Employees getById(int employeeId) {
		// TODO Auto-generated method stub
		return employeesRepository.findById(employeeId).get();
	}

	@Override
	public String validateduplicate(String email) {
		return employeesRepository.findByEmployeeEmail(email);
	}

	@Override
	public Employees getEmployee(String email, String password) {
		return employeesRepository.findByEmailAndPassword(email, password);
	}

	@Override
	public void saveEmployees(MultipartFile file, Employees employees) {
		 {
		employeesRepository.save(employees);
		}


	}
}
