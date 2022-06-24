package com.vcare.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vcare.beans.Doctor;
import com.vcare.beans.HospitalBranch;
import com.vcare.repository.DoctorRepository;

@Service
public class DoctorImplementation implements DoctorService {
	@Autowired
	DoctorRepository doctorRepository;

	@Override
	public void deleteDoctorsById(int doctorId) {
		doctorRepository.deleteById(doctorId);

	}

	@Override
	public void saveDoctor(Doctor doctors) {
		doctorRepository.save(doctors);
	}

	@Override
	public Doctor GetDocotorById(int doctorId) {

		return doctorRepository.getById(doctorId);
	}

	@Override
	public void updateDoctor(Doctor doctors) {
		doctorRepository.save(doctors);

	}

	@Override
	public Doctor addDoctor(Doctor doctors) {

		return doctorRepository.save(doctors);
	}

	@Override
	public List<Doctor> getAllDoctor() {

		return doctorRepository.findAll();
	}

	@Override
	public String validateduplicate(String doctorMailId) {

		return doctorRepository.findBydoctorMailIdIgnoreCase(doctorMailId);
	}

	@Override
	public Doctor getDoctors(String doctorMailId, String password) {

		return doctorRepository.findBydoctorMailIdIgnoreCaseAndPassword(doctorMailId, password);
	}

	@Override
	public Doctor findByMail(String doctorMailId) {
		// TODO Auto-generated method stub
		return doctorRepository.findByEmail(doctorMailId);
	}

	@Override
	public void updateDoctor(MultipartFile file, Doctor doctors) {
		// TODO Auto-generated method stub
		doctorRepository.save(doctors);
	}



	
}
