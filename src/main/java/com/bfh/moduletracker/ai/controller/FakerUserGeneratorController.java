package com.bfh.moduletracker.ai.controller;

import com.bfh.moduletracker.ai.common.FakeUserGenerator;
import com.bfh.moduletracker.ai.exceptions.InvalidModuleAssignmentException;
import com.bfh.moduletracker.ai.exceptions.UserAlreadyExistException;
import com.bfh.moduletracker.ai.exceptions.UserModuleAlreadyExistException;
import com.bfh.moduletracker.ai.service.loader.VectorStoreLoad;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fakerUserGenerator")
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "1.0", description = "Authentication and Authorization API"))
public class FakerUserGeneratorController {

    private static final Logger log = LoggerFactory.getLogger(FakerUserGeneratorController.class);
    private final FakeUserGenerator fakerUserGenerator;


    public FakerUserGeneratorController( FakeUserGenerator fakerUserGenerator) {
        this.fakerUserGenerator = fakerUserGenerator;

    }


    @PostMapping("/generate")
    public ResponseEntity<Void> getAll() throws UserAlreadyExistException, InvalidModuleAssignmentException, UserModuleAlreadyExistException {
        log.info("Fetching all faker users");
        fakerUserGenerator.run();
        return ResponseEntity.ok().build();
    }


}
