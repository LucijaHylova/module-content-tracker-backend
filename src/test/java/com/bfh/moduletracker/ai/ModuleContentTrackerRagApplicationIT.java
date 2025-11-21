package com.bfh.moduletracker.ai;


import com.bfh.moduletracker.ai.config.PostgresTestcontainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("inttest")
@Import(PostgresTestcontainerConfiguration.class)
@EnableAutoConfiguration()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModuleContentTrackerRagApplicationIT {


    @Test
    void contextLoads() {
    }

}
