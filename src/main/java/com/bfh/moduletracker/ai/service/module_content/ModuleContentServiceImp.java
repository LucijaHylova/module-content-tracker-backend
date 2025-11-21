package com.bfh.moduletracker.ai.service.module_content;


import java.util.List;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleCompetenceProfileResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleContentDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleContentProfileResponseDto;
import com.bfh.moduletracker.ai.model.entity.ModuleContent;
import com.bfh.moduletracker.ai.repository.ModuleContentRepository;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class ModuleContentServiceImp implements ModuleContentService {

    private final ModuleContentRepository moduleContentRepository;

    public ModuleContentServiceImp(ModuleContentRepository moduleContentRepository) {
        this.moduleContentRepository = moduleContentRepository;
    }


    @Override
    public void upsertModuleContent(ModuleContent moduleContent) {

        ModuleContent existing = moduleContentRepository.findByCode(moduleContent.getCode());

        ModuleContent entityToSave;
        if (existing != null) {
            entityToSave = ModuleContent.builder()
                    .code(existing.getCode())
                    .name(moduleContent.getName())
                    .competencies(moduleContent.getCompetencies())
                    .requirements(moduleContent.getRequirements())
                    .content(moduleContent.getContent())
                    .shortDescription(moduleContent.getShortDescription())
                    .responsibility(moduleContent.getResponsibility())
                    .ects(moduleContent.getEcts())
                    .selfStudy(moduleContent.getSelfStudy())
                    .build();
        } else {
            entityToSave = moduleContent;
        }

        moduleContentRepository.save(entityToSave);

    }

    @Override
    public ModuleContentDto getModuleContent(String code) {
        val result = moduleContentRepository.findByCode(code);
        return ModuleContentDto.builder()
                .code(result.getCode())
                .name(result.getName())
                .competencies(GeneralMapper.mapToLocalizedVisualDataEntryDtos(result.getCompetencies()))
                .requirements(GeneralMapper.mapToVisualDataEntryDtos(result.getRequirements()))
                .content(GeneralMapper.mapToLocalizedVisualDataEntryDtos(result.getContent()))
                .shortDescription(GeneralMapper.mapToVisualDataEntryDtos(result.getShortDescription()))
                .responsibility(result.getResponsibility())
                .ects(result.getEcts())
                .selfStudy(result.getSelfStudy())
                .build();
    }

    @Override
    public List<ModuleCompetenceProfileResponseDto> getModuleContentCompetencies() {
        return moduleContentRepository.findAll().stream().map(mc -> ModuleCompetenceProfileResponseDto.builder()
                .code(mc.getCode())
                .competencies(GeneralMapper.mapToLocalizedVisualDataEntryDtos(mc.getCompetencies()))
                .build()).toList();
    }

    @Override
    public List<ModuleContentProfileResponseDto> getModuleContentContents() {
        return moduleContentRepository.findAll().stream().map(mc -> ModuleContentProfileResponseDto.builder()
                .code(mc.getCode())
                .contents(GeneralMapper.mapToLocalizedVisualDataEntryDtos(mc.getContent()))
                .build()).toList();
    }
}
