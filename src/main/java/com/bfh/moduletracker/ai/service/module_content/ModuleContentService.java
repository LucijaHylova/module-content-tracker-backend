package com.bfh.moduletracker.ai.service.module_content;

import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.ModuleCompetenceProfileResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleContentDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleContentProfileResponseDto;
import com.bfh.moduletracker.ai.model.entity.ModuleContent;

public interface ModuleContentService {

    void upsertModuleContent(ModuleContent moduleContent);

    ModuleContentDto getModuleContent(String code);

    List<ModuleCompetenceProfileResponseDto> getModuleContentCompetencies();

    List<ModuleContentProfileResponseDto> getModuleContentContents();
}
