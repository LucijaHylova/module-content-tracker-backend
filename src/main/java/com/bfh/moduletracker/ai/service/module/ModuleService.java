package com.bfh.moduletracker.ai.service.module;

import java.util.List;

import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;

public interface ModuleService {

    List<ModuleDto> getModulesByCode(String code);

    ModuleDto getModuleByCode(String code);

    List<ModuleDto> getAllModules();
}
