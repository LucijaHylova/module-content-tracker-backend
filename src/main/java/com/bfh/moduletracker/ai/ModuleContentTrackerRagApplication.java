package com.bfh.moduletracker.ai;

import com.bfh.moduletracker.ai.service.loader.VectorStoreLoad;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ModuleContentTrackerRagApplication {

    private static final Logger log = LoggerFactory.getLogger(ModuleContentTrackerRagApplication.class);

    @Autowired
    private final VectorStoreLoad vectorStoreLoad;

    public ModuleContentTrackerRagApplication(VectorStoreLoad vectorStoreLoad) {

        this.vectorStoreLoad = vectorStoreLoad;
   
    }
    @PostConstruct
    @Profile("railway")
    public void initVectorStore() {
        vectorStoreLoad.run();
    }

    public static void main(String[] args) {
        log.atInfo().log("Starting application");
        SpringApplication.run(ModuleContentTrackerRagApplication.class, args);

    }

    @Bean
    InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

}
