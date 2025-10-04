package com.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.medicall.common.config.BaseSwaggerConfig;

@Configuration
@EnableWebSecurity
public class HospitalSwaggerConfig extends BaseSwaggerConfig {
    @Bean
    @Primary
    public OpenAPI doctorOpenAPI() {
        return super.customOpenAPI()
                .addTagsItem(new Tag()
                        .name("MyPage")
                        .description("마이 페이지 API"))
                .addTagsItem(new Tag()
                        .name("Doctor")
                        .description("의사 관련 API"))
                .addTagsItem(new Tag()
                        .name("Appointment")
                        .description("예약 관련 API"))
                .addTagsItem(new Tag()
                        .name("Patient")
                        .description("환자 관련 API"))
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
    public GroupedOpenApi hospitalOpenApi() {
        return GroupedOpenApi.builder()
                .group("2-hospital")
                .displayName("hospital API")
                .pathsToMatch("api/v1/hospital/**")
                .build();
    }
}
