package com.bfh.moduletracker.ai.model.dto.module;

import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.CompetenciesDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ContentDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.DepartmentDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ModuleTypeDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.NameDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ProgramDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.RequirementsDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ResponsibilityDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.SelfStudyDto;
import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.ShortDescriptionDto;
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

public class ModuleDto {
    private String code;

    private NameDto name;

    private DepartmentDto department;

    private ProgramDto program;

    private CompetenciesDto competencies;

    private RequirementsDto requirements;

    private Integer ects;

    private ShortDescriptionDto shortDescription;

    private ContentDto content;

    private ResponsibilityDto responsibility;

    private SelfStudyDto selfStudy;

    private double score;

    private ModuleTypeDto moduleType;

    private List<Integer> semesters;

    private SpecializationDto specialization;

}
