package com.bfh.moduletracker.ai.service.program_content;

import java.util.ArrayList;
import java.util.List;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.model.dto.ai.ProgramContentCompetencyDto;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.model.entity.ProgramContent;
import com.bfh.moduletracker.ai.model.entity.VisualDataEntry;
import com.bfh.moduletracker.ai.repository.ProgramContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProgramContentServiceImp implements ProgramContentService {


    private final ProgramContentRepository programContentRepository;

    public ProgramContentServiceImp(ProgramContentRepository programContentRepository) {
        this.programContentRepository = programContentRepository;
    }

    @Transactional
    public void upsertCompetenciesForProgram(
            String program,
            List<LocalizedVisualDataEntry> newCompetencies
    ) {
        ProgramContent entity = programContentRepository
                .findByProgram(program)
                .orElseGet(() -> {
                    return ProgramContent.builder()
                            .program(program)
                            .build();
                });

        entity.setCompetencies(new ArrayList<>(newCompetencies));

        programContentRepository.save(entity);
    }

    @Transactional
    public void upsertCompetenciesForProgramWithModuleTypeFilterOption(
            String program,
            List<LocalizedVisualDataEntry> newCompetencies,
            String filterTag
    ) {
        String fullProgramId = program + "_" + filterTag;

        ProgramContent entity = programContentRepository
                .findByProgram(fullProgramId)
                .orElseGet(() -> {
                    return ProgramContent.builder()
                            .program(fullProgramId)
                            .build();
                });

        entity.setCompetencies(new ArrayList<>(newCompetencies));

        programContentRepository.save(entity);
    }

    @Override
    public void upsertShortDescriptionForProgram(String program, List<VisualDataEntry> newShortDescription) {
        ProgramContent entity = programContentRepository
                .findByProgram(program)
                .orElseGet(() -> {
                    return ProgramContent.builder()
                            .program(program)
                            .build();
                });

        entity.setShortDescription(new ArrayList<>(newShortDescription));

        programContentRepository.save(entity);
    }

    @Transactional
    public void upsertShortDescriptionForProgramWithModuleTypeFilterOption(
            String program,
            List<VisualDataEntry> newShortDescription,
            String filterTag
    ) {
        String fullProgramId = program + "_" + filterTag;

        ProgramContent entity = programContentRepository
                .findByProgram(fullProgramId)
                .orElseGet(() -> {
                    return ProgramContent.builder()
                            .program(fullProgramId)
                            .build();
                });

        entity.setShortDescription(new ArrayList<>(newShortDescription));

        programContentRepository.save(entity);
    }

    @Override
    public List<ProgramContentCompetencyDto> getProgramContentCompetencies() {
        return programContentRepository.findAll().stream().map(mc -> ProgramContentCompetencyDto.builder()
                .program(mc.getProgram())
                .competencies(GeneralMapper.mapToLocalizedVisualDataEntryDtos(mc.getCompetencies()))
                .build()).toList();
    }


}

