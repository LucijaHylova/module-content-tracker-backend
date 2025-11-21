package com.bfh.moduletracker.ai.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
@Configuration
public class PostgresTestcontainerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PostgresTestcontainerConfiguration.class);

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine");

        container.start();

        log.atInfo().log("PostgreSQL Testcontainer gestartet:");
        log.atInfo().log("JDBC URL:  " + container.getJdbcUrl());
        log.atInfo().log("Username:  " + container.getUsername());
        log.atInfo().log("Password:  " + container.getPassword());

        return container;
    }

}
