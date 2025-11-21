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
public class ProgramContentCompetencyDto {

    private String program;
    public List<LocalizedVisualDataEntryDto> competencies;

}
