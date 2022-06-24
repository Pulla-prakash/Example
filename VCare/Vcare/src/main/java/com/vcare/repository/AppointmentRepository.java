package com.vcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vcare.beans.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	@Query(value="select al from Appointment al where al.doctor.doctorId=?1 and al.date=?2")
	List<Appointment> appointmentListByDoctorid(int doctorId,String date);

}
