package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.hospital",
        "com.medicall.domain",
        "com.medicall.common",
        "com.medicall.storage"
})
@ConfigurationPropertiesScan("com.hospital.config")
@EnableJpaRepositories(basePackages = "com.medicall.storage.db.domain")
@EntityScan(basePackages = "com.medicall.storage.db.domain")
public class HospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
}
