package com.vcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.vcare.beans.Appointment;
import com.vcare.repository.AppointmentRepository;

@Service
public class AppointmentServiceImp implements AppointmentService {

	// Appointment Service
	@Autowired
	AppointmentRepository applointmentRepository;

	@Override
	public List<Appointment> getAllAppointment() {
		return applointmentRepository.findAll();
	}

	@Override
	public Appointment getAppointmentById(long appointmentId) {
		return applointmentRepository.getById(appointmentId);
	}

	@Override
	public Appointment addAppointment(Appointment appointment) {
		return applointmentRepository.save(appointment);
	}

	@Override
	public void updateAppointment(Appointment appointment) {
		applointmentRepository.save(appointment);

	}

	@Override
	public void deleteAppointmentById(long appointmentId) {
		try {
			applointmentRepository.deleteById(appointmentId);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

}
