package com.bfh.moduletracker.ai.controller;

import java.io.IOException;

import com.bfh.moduletracker.ai.repository.UserRepository;
import com.bfh.moduletracker.ai.service.loader.ModuleLoader;
import com.bfh.moduletracker.ai.service.loader.ModuleReader;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/modules")
@OpenAPIDefinition(
        info = @Info(
                title = "Module Import API",
                version = "1.0",
                description = "API for importing modules"
        ))
public class ModuleImportController {


    private final
    ModuleLoader moduleLoader;

    private final
    ModuleReader moduleReader;
    private final UserRepository userEntityRepository;


    public ModuleImportController(@Autowired(required = false) ModuleLoader moduleLoader, ModuleReader moduleReader, UserRepository userEntityRepository) {
        this.moduleLoader = moduleLoader;
        this.moduleReader = moduleReader;
        this.userEntityRepository = userEntityRepository;
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(responseCode = "200", description = "File imported successfully")
    public ResponseEntity<String> importModules(@RequestParam("file") MultipartFile file) {

        // accessController.verifyAccess(customer);
        try {

            moduleLoader.load(moduleReader.read(file.getInputStream()));

            return ResponseEntity.ok("File imported " + file.getOriginalFilename());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }
}


