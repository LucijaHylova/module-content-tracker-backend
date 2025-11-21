package com.bfh.moduletracker.ai.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

public class EnvProcessor implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(EnvProcessor.class);


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(".env"));
            env.getPropertySources()
                    .addFirst(new PropertiesPropertySource("envProperties", props));
            log.atInfo().log("Loaded .env file successfully.");
        } catch (IOException e) {
            log.atError().log("Failed to load .env file: {}", e.getMessage());

        }
    }
}
