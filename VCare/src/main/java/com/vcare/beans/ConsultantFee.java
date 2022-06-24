package com.vcare.beans;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//Abilash
@Entity
@Table(name = "consultantsfee")
public class ConsultantFee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "consultant_id")
	private int ConsultantId;
	@Column(name = "consultant_fee")
	private int ConsultantFee;

	@Column(columnDefinition = "character(1) DEFAULT 'Y'::bpchar")
	private char isactive;
	private String createdBy;
//	private String updateBy;
//	private Date updated;
	private LocalDate created;

	@ManyToOne
	@JoinColumn(name = "DoctorId")
	public Doctor doctor;

	public int getConsultantId() {
		return ConsultantId;
	}

	public void setConsultantId(int consultantId) {
		ConsultantId = consultantId;
	}

	public int getConsultantFee() {
		return ConsultantFee;
	}

	public void setConsultantFee(int consultantFee) {
		ConsultantFee = consultantFee;
	}

	public char getIsactive() {
		return isactive;
	}

	public void setIsactive(char isactive) {
		this.isactive = isactive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

}
