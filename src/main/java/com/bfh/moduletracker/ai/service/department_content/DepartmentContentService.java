package com.bfh.moduletracker.ai.service.department_content;


import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.DepartmentContentCompetencyDto;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;

public interface DepartmentContentService {

    void upsertCompetenciesForDepartment(String department, List<LocalizedVisualDataEntry> newCompetencies);

    List<DepartmentContentCompetencyDto> getDepartmentContentCompetencies();
}
