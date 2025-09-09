package com.medicall.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseSwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String apiVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + "API")
                        .version(apiVersion)
                        .description(applicationName + "서비스 API 문서입니다.")
                        .contact(new Contact()
                                .name("Medicall 개발자 SH-Seol")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
