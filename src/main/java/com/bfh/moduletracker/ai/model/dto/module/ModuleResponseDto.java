package com.bfh.moduletracker.ai.model.dto.module;

import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.DepartmentDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ModuleTypeDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.NameDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ProgramDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ResponsibilityDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.SpecializationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class ModuleResponseDto {
    private String code;

    private NameDto name;

    private DepartmentDto department;

    private ProgramDto program;

    private Integer ects;

    private ResponsibilityDto responsibility;

    private ModuleTypeDto moduleType;

    private List<Integer> semesters;

    private SpecializationDto specialization;

    private String status;


}
