package com.bfh.moduletracker.ai.controller;

import java.util.List;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.model.dto.module.ModuleResponseDto;
import com.bfh.moduletracker.ai.service.module.ModuleService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/modules")
@OpenAPIDefinition(info = @Info(title = "Module API", version = "1.0", description = "API for managing modules"))
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(
            ModuleService moduleService
    ) {
        this.moduleService = moduleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ModuleResponseDto>> getModules() {
        log.info("Fetching all modules");
        List<ModuleDto> modules = moduleService.getAllModules();

        val response = modules.stream().map(
                GeneralMapper::mapToModuleResponseDto
        ).toList();

        log.atInfo().log("Total modules fetched: {}", response.size());

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("get/{code}")
    public ResponseEntity<ModuleResponseDto> getModuleByCode(@PathVariable String code) {
        val module = moduleService.getModuleByCode(code);
        val response = GeneralMapper.mapToModuleResponseDto(module);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
