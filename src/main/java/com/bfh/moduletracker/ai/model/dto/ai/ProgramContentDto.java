package com.bfh.moduletracker.ai.model.dto.ai;

import java.util.List;

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
public class ProgramContentDto {

    public String program;

    public List<VisualDataEntryDto> competencies;

    public List<VisualDataEntryDto> requirements;

    public List<VisualDataEntryDto> shortDescription;

    public List<VisualDataEntryDto> content;

    public String responsibility;

}
