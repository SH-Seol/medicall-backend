package com.medicall.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("/");
        return new OpenAPI()
                .info(new Info()
                        .title("Medicall Hospital API")
                        .description("의료 방문 서비스 Medicall 병원 서비스 API입니다.")
                        .version("1.0.0"))
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 토큰을 입력하세요")));
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
