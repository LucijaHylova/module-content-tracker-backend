package com.bfh.moduletracker.ai.service.program_content;

import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.LocalizedVisualDataEntryDto;
import com.bfh.moduletracker.ai.model.dto.ai.ProgramContentCompetencyDto;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.model.entity.VisualDataEntry;

public interface ProgramContentService {

    void upsertCompetenciesForProgram(String program, List<LocalizedVisualDataEntry> newCompetencies);

    void upsertShortDescriptionForProgram(String program, List<VisualDataEntry> newShortDescription);

    List<ProgramContentCompetencyDto> getProgramContentCompetencies();

}
