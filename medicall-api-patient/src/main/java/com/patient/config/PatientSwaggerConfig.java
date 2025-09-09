package com.patient.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.medicall.common.config.BaseSwaggerConfig;

@Configuration
@EnableWebSecurity
public class PatientSwaggerConfig extends BaseSwaggerConfig {
    @Bean
    @Primary
    public OpenAPI patientOpenAPI() {
        return super.customOpenAPI()
                .addTagsItem(new Tag()
                        .name("MyPage")
                        .description("마이 페이지 API"))
                .addTagsItem(new Tag()
                        .name("Hospital")
                        .description("병원 관련 API"))
                .addTagsItem(new Tag()
                        .name("Doctor")
                        .description("의사 관련 API"))
                .addTagsItem(new Tag()
                        .name("Appointment")
                        .description("예약 관련 API"))
                .addTagsItem(new Tag()
                        .name("Treatment")
                        .description("진단 관련 API"))
                .addTagsItem(new Tag()
                        .name("Prescription")
                        .description("처방 관련 API"))
                .addTagsItem(new Tag()
                        .name("Chat")
                        .description("채팅 관련 API"));
    }

    @Bean
    public GroupedOpenApi patientOpenApi() {
        return GroupedOpenApi.builder()
                .group("3-patient")
                .displayName("patient API")
                .pathsToMatch("api/v1/patient/**")
                .build();
    }
}
