package com.bfh.moduletracker.ai.controller;


import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.ModuleContentProfileResponseDto;
import com.bfh.moduletracker.ai.service.department_content.DepartmentContentService;
import com.bfh.moduletracker.ai.service.module_content.ModuleContentService;
import com.bfh.moduletracker.ai.service.program_content.ProgramContentService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/content")
@OpenAPIDefinition(info = @Info(title = "AI Service", version = "1.0"))
public class ContentProfileController {

    private final ModuleContentService moduleContentService;

    public ContentProfileController(
            ModuleContentService moduleContentService, ProgramContentService programContentService, DepartmentContentService departmentContentService
    ) {
        this.moduleContentService = moduleContentService;
    }

    @GetMapping("/getModuleContentProfiles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved content profiles for module")
    @ApiResponse(responseCode = "404", description = "Module profiles not found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<List<ModuleContentProfileResponseDto>> getModuleProfiles() {
        log.info("Fetching all module profiless");
        List<ModuleContentProfileResponseDto> profiles = moduleContentService.getModuleContentContents();

        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }


}
