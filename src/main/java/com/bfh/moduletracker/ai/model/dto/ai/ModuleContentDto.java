package com.bfh.moduletracker.ai.model.dto.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ModuleContentDto {
    public String code;

    public String name;

    public String department;

    public String program;

    public List<LocalizedVisualDataEntryDto> competencies;

    public List<VisualDataEntryDto> requirements;

    public String ects;

    public List<VisualDataEntryDto> shortDescription;

    public List<LocalizedVisualDataEntryDto> content;

    public List<String> responsibility;

    public String selfStudy;

    public double score;

}
