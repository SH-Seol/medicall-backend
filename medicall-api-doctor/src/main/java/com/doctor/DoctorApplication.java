package com.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.medicall.domain",
		"com.medicall.common"
})
@ConfigurationPropertiesScan("com.doctor.config")
public class DoctorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorApplication.class, args);
	}

}
