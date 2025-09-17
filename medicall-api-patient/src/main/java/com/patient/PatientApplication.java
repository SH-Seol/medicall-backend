package com.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.medicall.common.config.HealthCheckConfig;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.medicall.domain",
        "com.medicall.common"
})
public class PatientApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }
}
