package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.medicall.domain",
        "com.medicall.common"
})
@ConfigurationPropertiesScan("com.hospital.config")
public class HospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
}
