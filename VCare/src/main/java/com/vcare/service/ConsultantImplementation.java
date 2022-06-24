package com.vcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcare.beans.ConsultantFee;
import com.vcare.repository.ConsultantRepository;

@Service
public class ConsultantImplementation implements ConsultantService {

	@Autowired
	ConsultantRepository consultantrepository;

	@Override
	public List<ConsultantFee> getAllconsultent() {

		return consultantrepository.findAll();
	}

	@Override
	public void deleteconsultentById(int ConsultentId) {
		consultantrepository.deleteById(ConsultentId);

	}

	@Override
	public void saveconsultent(ConsultantFee consultentfee) {

		consultantrepository.save(consultentfee);
	}

	@Override
	public ConsultantFee GetconsultentById(int ConsultentId) {

		return consultantrepository.getById(ConsultentId);
	}

	@Override
	public void updateconsultentfee(ConsultantFee consultentfee) {
		consultantrepository.save(consultentfee);

	}

	@Override
	public ConsultantFee addconsultent(ConsultantFee consultentfee) {

		return consultantrepository.save(consultentfee);
	}

}
