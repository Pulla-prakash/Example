package com.vcare.service;

import java.util.List;

import com.vcare.beans.ConsultantFee;

public interface ConsultantService {

	List<ConsultantFee> getAllconsultent();

	void deleteconsultentById(int ConsultentId);

	void saveconsultent(ConsultantFee consultentfee);

	ConsultantFee GetconsultentById(int ConsultentId);

	void updateconsultentfee(ConsultantFee consultentfee);

	ConsultantFee addconsultent(ConsultantFee consultentfee);

}
