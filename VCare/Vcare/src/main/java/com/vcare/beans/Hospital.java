package com.vcare.beans;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//Prakash
@Entity
@Table(name = "hospital")
public class Hospital {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int hospitalId;
	private String hospitalName;
	private String hospitalLocation;
	private long hospitalNumber;
	private String hospitalAddress;
	private int hospitalPincode;
	private String hospitalState;
	private String hospitalCity;
	private String hospitalEmailId;
	private String hospitalLicense;
	private String description;
	private String storeLink;
	private String appStoreLink;

	@Column(columnDefinition = "character(1) DEFAULT 'Y'::bpchar")
	private Character isactive;

	private String createdBy;

	private String updateBy;

	private LocalDate updated;
	@Column(columnDefinition = "date default now()")
	private LocalDate created;

	@OneToMany(mappedBy = ("hospital"), cascade = CascadeType.ALL)
	private List<Services> services;

	@OneToMany(mappedBy = ("hospital"), cascade = CascadeType.ALL)
	private List<SocialNetworks> socialNetworks;

	public List<Services> getServices() {
		return services;
	}

	public void setServices(List<Services> services) {
		this.services = services;
	}

	@OneToMany(mappedBy = ("hospitals"), cascade = CascadeType.ALL)
	private List<HospitalBranch> hospitalBranch;

	public LocalDate getUpdated() {
		return updated;
	}

	public String getAppStoreLink() {
		return appStoreLink;
	}

	public void setAppStoreLink(String appStoreLink) {
		this.appStoreLink = appStoreLink;
	}

	public void setUpdated(LocalDate updated) {
		this.updated = updated;
	}

	public LocalDate getCreated() {
		return created;
	}

	public String getStoreLink() {
		return storeLink;
	}

	public void setStoreLink(String storeLink) {
		this.storeLink = storeLink;
	}

	public void setCreated(LocalDate localDate) {
		this.created = localDate;
	}

	public void setIsactive(Character isactive) {
		this.isactive = isactive;
	}

	public char getIsactive() {
		return isactive;
	}

	public void setIsactive(char isactive) {
		this.isactive = isactive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SocialNetworks> getSocialNetworks() {
		return socialNetworks;
	}

	public void setSocialNetworks(List<SocialNetworks> socialNetworks) {
		this.socialNetworks = socialNetworks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setHospitalBranch(List<HospitalBranch> hospitalBranch) {
		this.hospitalBranch = hospitalBranch;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalLocation() {
		return hospitalLocation;
	}

	public void setHospitalLocation(String hospitalLocation) {
		this.hospitalLocation = hospitalLocation;
	}

	public long getHospitalNumber() {
		return hospitalNumber;
	}

	public void setHospitalNumber(long hospitalNumber) {
		this.hospitalNumber = hospitalNumber;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public int getHospitalPincode() {
		return hospitalPincode;
	}

	public void setHospitalPincode(int hospitalPincode) {
		this.hospitalPincode = hospitalPincode;
	}

	public String getHospitalState() {
		return hospitalState;
	}

	public void setHospitalState(String hospitalState) {
		this.hospitalState = hospitalState;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getHospitalEmailId() {
		return hospitalEmailId;
	}

	public String getHospitalLicense() {
		return hospitalLicense;
	}

	public void setHospitalLicense(String hospitalLicense) {
		this.hospitalLicense = hospitalLicense;
	}

	public void setHospitalEmailId(String hospitalEmailId) {
		this.hospitalEmailId = hospitalEmailId;
	}

	public List<HospitalBranch> getHospitalBranch() {
		return hospitalBranch;
	}

	public void setHospitalBranches(List<HospitalBranch> hospitalBranch) {
		this.hospitalBranch = hospitalBranch;
	}

}
