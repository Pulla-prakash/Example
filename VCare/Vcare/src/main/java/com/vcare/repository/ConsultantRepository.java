package com.vcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcare.beans.ConsultantFee;

@Repository
public interface ConsultantRepository extends JpaRepository<ConsultantFee,Integer> {

}
