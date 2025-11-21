package com.bfh.moduletracker.ai;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ModuleContentTrackerRagApplication {

    private static final Logger log = LoggerFactory.getLogger(ModuleContentTrackerRagApplication.class);

    public static void main(String[] args) {
        log.atInfo().log("Starting application");
        SpringApplication.run(ModuleContentTrackerRagApplication.class, args);

    }

    @Bean
    InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @PostConstruct
    public void init(@Autowired DataSource ds) throws Exception {
        System.out.println(">>> JDBC URL = " + ds.getConnection().getMetaData().getURL());
    }
}
