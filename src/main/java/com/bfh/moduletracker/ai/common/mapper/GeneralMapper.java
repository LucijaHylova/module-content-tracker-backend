package com.bfh.moduletracker.ai.common.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.bfh.moduletracker.ai.model.auth.ModuleComparisonDto;
import com.bfh.moduletracker.ai.model.auth.RegisterUserRequestDto;
import com.bfh.moduletracker.ai.model.dto.ai.LocalizedVisualDataEntryDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.VisualDataEntryDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.DepartmentDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ModuleTypeDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.NameDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ProgramDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ResponsibilityDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.SpecializationDto;
import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.model.dto.module.ModuleResponseDto;
import com.bfh.moduletracker.ai.model.dto.user.UserResponseDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleDto;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.ModuleComparison;
import com.bfh.moduletracker.ai.model.entity.Name;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.model.entity.UserModuleId;
import com.bfh.moduletracker.ai.model.entity.VisualDataEntry;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class GeneralMapper {

    public static ModuleComparison mapToModuleComparison(ModuleComparisonResponseDto dto) {
        var moduleComparison = ModuleComparison.builder()
                .code(dto.getCode())
                .name(Name.builder()
                        .nameDe(dto.getName().getDe())
                        .nameEn(dto.getName().getEn())
                        .build())
                .build();
        moduleComparison.setDifferences_en(dto.getDifferences_en());
        moduleComparison.setDifferences_de(dto.getDifferences_de());
        moduleComparison.setSimilarities_en(dto.getSimilarities_en());
        moduleComparison.setSimilarities_de(dto.getSimilarities_de());
        return moduleComparison;
    }

    public static ModuleComparisonDto mapToModuleComparisonDto(ModuleComparison entity) {
        return ModuleComparisonDto.builder()
                .code(entity.getCode())
                .name(NameDto.builder()
                        .de(entity.getName().getNameDe())
                        .en(entity.getName().getNameEn())
                        .build())
                .differences_de(entity.getDifferences_de())
                .differences_en(entity.getDifferences_en())
                .similarities_de(entity.getSimilarities_de())
                .similarities_en(entity.getSimilarities_en())
                .build();
    }

    public static ModuleComparisonDto mapToModuleComparisonDto(ModuleComparisonResponseDto dto) {
        return ModuleComparisonDto.builder()
                .code(dto.getCode())
                .name(NameDto.builder()
                        .de(dto.getName().getDe())
                        .en(dto.getName().getEn())
                        .build())
                .differences_de(dto.getDifferences_de())
                .differences_en(dto.getDifferences_en())
                .similarities_de(dto.getSimilarities_de())
                .similarities_en(dto.getSimilarities_en())
                .build();
    }

    public UserModuleId mapToModuleUserId(String username, String moduleCode) {
        return UserModuleId.builder()
                .username(username)
                .moduleCode(moduleCode)
                .build();
    }

    public UserModule mapToUserModule(User user, Module module) {
        val userModuleId = mapToModuleUserId(user.getUsername(), module.getCode());
        return UserModule.builder()
                .id(userModuleId)
                .user(user)
                .module(module)
                .build();
    }


    public ModuleResponseDto mapToModuleResponseDto(ModuleDto module) {
        return ModuleResponseDto.builder()
                .code(module.getCode())
                .name(module.getName())
                .department(module.getDepartment())
                .program(module.getProgram())
                .ects(module.getEcts())
                .responsibility(module.getResponsibility())
                .moduleType(module.getModuleType())
                .semesters(module.getSemesters())
                .specialization(module.getSpecialization())
                .build();

    }

    public ModuleResponseDto mapToModuleResponseDto(Module module) {
        return ModuleResponseDto.builder()
                .code(module.getCode())
                .name(NameDto.builder()
                        .de(module.getName().getNameDe())
                        .en(module.getName().getNameEn())
                        .build())
                .department(DepartmentDto.builder()
                        .de(module.getDepartment().getDepartmentDe())
                        .en(module.getDepartment().getDepartmentEn())
                        .build())
                .program(ProgramDto.builder()
                        .de(module.getProgram().getProgramDe())
                        .en(module.getProgram().getProgramEn())
                        .build())
                .ects(Integer.valueOf(module.getEcts()))
                .responsibility(ResponsibilityDto.builder()
                        .de(module.getResponsibility().getResponsibilityDe())
                        .en(module.getResponsibility().getResponsibilityEn())
                        .build())
                .moduleType(ModuleTypeDto.builder()
                        .de(module.getModuleType().getModuleTypeDe())
                        .en(module.getModuleType().getModuleTypeEn())
                        .build())
                .semesters(module.getSemesters())

                .specialization(SpecializationDto.builder()
                        .de(module.getSpecialization().getSpecializationDe())
                        .en(module.getSpecialization().getSpecializationEn())
                        .build())
                .build();

    }

    public UserModuleDto mapToUserModuleDto(UserModule userModule) {

        return UserModuleDto.builder()
                .code(userModule.getModule().getCode())
                .username(userModule.getUser().getUsername())
                .name(NameDto.builder()
                        .de(userModule.getModule().getName().getNameDe())
                        .en(userModule.getModule().getName().getNameEn())
                        .build())
                .selectedSemester(userModule.getSelectedSemester())
                .status(userModule.getStatus())
                .build();
    }


    public UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .program(user.getProgram())
                .specialization(user.getSpecialization())
                .userModules(
                        user.getUserModules().stream()
                                .map(GeneralMapper::mapToUserModuleDto)
                                .collect(Collectors.toSet())
                )
                .moduleComparisons(
                        user.getModuleComparisons().stream()
                                .map(GeneralMapper::mapToModuleComparisonDto)
                                .collect(Collectors.toList())
                )
                .totalPassedEcts(user.getTotalPassedEcts())
                .build();
    }


    public User mapToUser(RegisterUserRequestDto registerUserRequest) {
        return User.builder()
                .username(registerUserRequest.getUsername())
                .email(registerUserRequest.getEmail())
                .password(registerUserRequest.getPassword())
                .program(registerUserRequest.getProgram())
                .specialization(registerUserRequest.getSpecialization())
                .moduleComparisons(Collections.emptyList())
                .build();
    }

    public List<VisualDataEntry> mapToVisualDataEntries(List<VisualDataEntryDto> dtoEntries) {
        if (dtoEntries == null) return null;
        return dtoEntries.stream()
                .map(dto -> VisualDataEntry.builder()
                        .key(dto.getKey())
                        .value(dto.getValue())
                        .build())
                .toList();

    }
    public List<LocalizedVisualDataEntry> mapToLocalizedVisualDataEntries(List<LocalizedVisualDataEntryDto> dtoEntries) {
        if (dtoEntries == null) return null;
        return dtoEntries.stream()
                .map(dto -> LocalizedVisualDataEntry.builder()
                        .key_de(dto.getKey_de())
                        .key_en(dto.getKey_en())
                        .value(dto.getValue())
                        .build())
                .toList();
    }



    public List<VisualDataEntryDto> mapToVisualDataEntryDtos(List<VisualDataEntry> entries) {
        if (entries == null) return null;
        return entries.stream()
                .map(e -> VisualDataEntryDto.builder()
                        .key(e.getKey())
                        .value(e.getValue())
                        .build())
                .toList();
    }

    public List<LocalizedVisualDataEntryDto> mapToLocalizedVisualDataEntryDtos(List<LocalizedVisualDataEntry> entries) {
        if (entries == null) return null;
        return entries.stream()
                .map(e -> LocalizedVisualDataEntryDto.builder()
                        .key_de(e.getKey_de())
                        .key_en(e.getKey_en())
                        .value(e.getValue())
                        .build())
                .toList();
    }
}
