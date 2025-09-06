package com.medicall.common.health;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/health")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String port;

    @GetMapping("/")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", applicationName,
                "port", port,
                "timestamp", LocalDateTime.now(),
                "message", applicationName + "is running");
    }

    @GetMapping("ready")
    public Map<String, Object> healthReady() {
        return Map.of(
                "status", "READY",
                "service", applicationName,
                "timestamp", LocalDateTime.now()
        );
    }

    @GetMapping("live")
    public Map<String, Object> healthLive() {
        return Map.of(
                "status", "ALIVE",
                "service", applicationName,
                "timestamp", LocalDateTime.now()
        );
    }
}
