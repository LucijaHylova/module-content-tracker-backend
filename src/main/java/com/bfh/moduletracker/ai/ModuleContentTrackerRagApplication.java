package com.bfh.moduletracker.ai;

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

    @Autowired DataSource ds;

}
