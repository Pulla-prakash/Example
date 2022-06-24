package com.vcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcare.beans.Department;

import com.vcare.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired

	DepartmentRepository departmentRepository;

	@Override
	public List<Department> getAllDepartments() {

		return departmentRepository.findAll();
	}

	@Override
	public Department addDepartment(Department department) {

		return departmentRepository.save(department);
	}

	@Override
	public void updateDepartment(Department department) {
		departmentRepository.save(department);
	}

	@Override
	public void deleteDepartmentById(int departmentId) {
		departmentRepository.deleteById(departmentId);
	}

	@Override
	public Department getDepartmentById(int departmentId) {
		return departmentRepository.findById(departmentId).get();
	}

}