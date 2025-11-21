package com.bfh.moduletracker.ai.service.department_content;

import java.util.ArrayList;
import java.util.List;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.model.dto.ai.DepartmentContentCompetencyDto;
import com.bfh.moduletracker.ai.model.entity.DepartmentContent;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.repository.DepartmentContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepartmentContentServiceImp implements DepartmentContentService {


    private final DepartmentContentRepository departmentContentRepository;

    public DepartmentContentServiceImp(DepartmentContentRepository departmentContentRepository) {
        this.departmentContentRepository = departmentContentRepository;
    }

    @Transactional
    public void upsertCompetenciesForDepartment(
            String department,
            List<LocalizedVisualDataEntry> newCompetencies
    ) {
        DepartmentContent entity = departmentContentRepository
                .findByDepartment(department)
                .orElseGet(() -> {
                    return DepartmentContent.builder()
                            .department(department)
                            .build();
                });


        entity.setCompetencies(new ArrayList<>(newCompetencies));

        departmentContentRepository.save(entity);
    }

    @Override
    public List<DepartmentContentCompetencyDto> getDepartmentContentCompetencies() {
        return departmentContentRepository.findAll().stream()
                .map(dc -> DepartmentContentCompetencyDto.builder()
                .department(dc.getDepartment())
                        .competencies(GeneralMapper.mapToLocalizedVisualDataEntryDtos(dc.getCompetencies()))
                .build()).toList();
    }
}

