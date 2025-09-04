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

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("/");
        return new OpenAPI()
                .info(new Info()
                        .title("Medicall Doctor API")
                        .description("의료 방문 서비스 Medicall 의사 서비스 API입니다.")
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
    public GroupedOpenApi doctorOpenApi() {
        return GroupedOpenApi.builder()
                .group("1-doctor")
                .displayName("doctor API")
                .pathsToMatch("api/v1/doctor/**")
                .build();
    }
}
