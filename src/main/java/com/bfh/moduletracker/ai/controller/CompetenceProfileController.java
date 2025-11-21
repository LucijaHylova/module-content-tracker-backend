package com.bfh.moduletracker.ai.controller;


import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.DepartmentContentCompetencyDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleCompetenceProfileResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.ProgramContentCompetencyDto;
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
public class CompetenceProfileController {

    private final ModuleContentService moduleContentService;
    private final ProgramContentService programContentService;
    private final DepartmentContentService departmentContentService;

    public CompetenceProfileController(
            ModuleContentService moduleContentService, ProgramContentService programContentService, DepartmentContentService departmentContentService
    ) {
        this.moduleContentService = moduleContentService;
        this.programContentService = programContentService;
        this.departmentContentService = departmentContentService;
    }

    @GetMapping("/getModuleCompetenceProfiles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved competence profiles for module")
    @ApiResponse(responseCode = "404", description = "Module profiles not found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<List<ModuleCompetenceProfileResponseDto>> getModuleProfiles() {
        log.info("Fetching all module profiless");
        List<ModuleCompetenceProfileResponseDto> profiles = moduleContentService.getModuleContentCompetencies();

        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    @GetMapping("/getProgramCompetenceProfiles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved competence profile for module")
    @ApiResponse(responseCode = "404", description = "Program profiles not found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<List<ProgramContentCompetencyDto>> getProgramCompetencyProfiles() {
        log.info("Fetching all program competency profiles");
        List<ProgramContentCompetencyDto> profiles = programContentService.getProgramContentCompetencies();

        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    @GetMapping("/getDepartmentCompetenceProfiles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved competence profile for department")
    @ApiResponse(responseCode = "404", description = "Department profilesnot found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<List<DepartmentContentCompetencyDto>> getDepartmentCompetencyProfiles() {
        log.info("Fetching all department competency profiles");
        List<DepartmentContentCompetencyDto> profiles = departmentContentService.getDepartmentContentCompetencies();

        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }


}
