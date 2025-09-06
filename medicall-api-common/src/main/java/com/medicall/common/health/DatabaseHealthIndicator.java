package com.medicall.common.health;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try(Connection connection = this.dataSource.getConnection()) {
            if(connection.isValid(1)){
                return Health.up()
                        .withDetail("DB", "Available")
                        .withDetail("validationQuery", "Connection is valid")
                        .build();
            }
            else {
                return Health.down()
                        .withDetail("DB", "Connection validation failed")
                        .build();
            }
        } catch(SQLException e){
            return Health.down()
                    .withDetail("DB", "Connection failed")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
