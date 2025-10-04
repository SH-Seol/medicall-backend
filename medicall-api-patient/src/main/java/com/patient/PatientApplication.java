package com.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.patient",
        "com.medicall.domain",
        "com.medicall.common",
        "com.medicall.storage"
})
@ConfigurationPropertiesScan("com.patient.config")
@EnableJpaRepositories(basePackages = "com.medicall.storage.db.domain")
@EntityScan(basePackages = "com.medicall.storage.db.domain")
public class PatientApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }
}
