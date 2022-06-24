package com.vcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages= {"com.vcare.controller","com.vcare.service","com.vcare.utils"})
@EntityScan("com.vcare.beans")
@EnableJpaRepositories("com.vcare.repository")
public class VcareProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(VcareProjectApplication.class, args);
	}

}
