package com.bfh.moduletracker.ai.service.ai;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Order(3)
public class StartupEndpointCaller implements CommandLineRunner {

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(StartupEndpointCaller.class);

    public StartupEndpointCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) {
        List<String> endpoints = List.of(
                "http://localhost:8082/ai/getProgramContentShortDescriptionData",
                "http://localhost:8082/ai/getProgramContentShortDescriptionData?moduleType=Pflichtmodul",
                "http://localhost:8082/ai/getProgramContentShortDescriptionData?moduleType=Wahlmodul",
                "http://localhost:8082/ai/getProgramContentShortDescriptionData?moduleType=Wahlpflichtmodul",

                "http://localhost:8082/ai/getProgramContentCompetenciesData",
                "http://localhost:8082/ai/getProgramContentCompetenciesData?moduleType=Pflichtmodul",
                "http://localhost:8082/ai/getProgramContentCompetenciesData?moduleType=Wahlmodul (anrechenbar)",
                "http://localhost:8082/ai/getProgramContentCompetenciesData?moduleType=Wahlpflichtmodul",

                "http://localhost:8082/ai/getModuleContentData",
                "http://localhost:8082/ai/getDepartmentContentCompetenciesData");

        for (String endpoint : endpoints) {
            try {
                log.atInfo().log("Calling: " + endpoint);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                log.atInfo().log("Response: " + response.getStatusCode() + "\n" + response.getBody());
            } catch (Exception e) {
                log.atError().log("Error calling " + endpoint + ": " + e.getMessage());
            }
        }
        log.atInfo().log("Startup endpoint calls completed.");
    }
}

