package com.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.doctor",
		"com.medicall.domain",
		"com.medicall.common",
		"com.medicall.storage",
		"com.medicall.chat"
})
@ConfigurationPropertiesScan("com.doctor.config")
@EnableJpaRepositories(basePackages = "com.medicall.storage.db.domain")
@EntityScan(basePackages = "com.medicall.storage.db.domain")
public class DoctorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorApplication.class, args);
	}

}
