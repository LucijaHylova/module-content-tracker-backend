package com.bfh.moduletracker.ai.service.module;

import java.util.Comparator;
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
import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImp implements ModuleService {

    private final ModuleRepository moduleRepository;
    private static final Logger log = LoggerFactory.getLogger(ModuleServiceImp.class);

    public ModuleServiceImp(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<ModuleDto> getAllModules() {
        log.atInfo().log("Getting all modules");
        List<Module> results = this.moduleRepository.findAll();
        return results
                .stream()
                .map(module -> {
                    return ModuleDto.builder()
                            .code(module.getCode())
                            .name(NameDto.builder()
                                    .de(module.getName().getNameDe())
                                    .en(module.getName().getNameEn())
                                    .fr(module.getName().getNameFr())
                                    .build())
                            .department(DepartmentDto.builder()
                                    .de(module.getDepartment().getDepartmentDe())
                                    .en(module.getDepartment().getDepartmentEn())
                                    .fr(module.getDepartment().getDepartmentFr())
                                    .build())
                            .program(ProgramDto.builder()
                                    .de(module.getProgram().getProgramDe())
                                    .en(module.getProgram().getProgramEn())
                                    .fr(module.getProgram().getProgramFr())
                                    .build())
                            .competencies(CompetenciesDto.builder()
                                    .de(module.getCompetencies().getCompetenciesDe())
                                    .build())
                            .requirements(RequirementsDto.builder()
                                    .de(module.getRequirements().getRequirementsDe())
                                    .build())
                            .ects(
                                    (module.getEcts() != null && !module.getEcts().isBlank())
                                            ? Integer.parseInt(module.getEcts().trim())
                                            : 0
                            )
                            .shortDescription(ShortDescriptionDto.builder()
                                    .de(module.getShortDescription().getShortDescriptionDe())
                                    .build())
                            .content(ContentDto.builder()
                                    .de(module.getContent().getContentDe())
                                    .build())
                            .selfStudy(SelfStudyDto.builder()
                                    .de(module.getSelfStudy().getSelfStudyDe())
                                    .build())
                            .responsibility(ResponsibilityDto.builder()
                                    .de(module.getResponsibility().getResponsibilityDe())
                                    .build())
                            .moduleType(ModuleTypeDto.builder()
                                    .de(module.getModuleType().getModuleTypeDe())
                                    .en(module.getModuleType().getModuleTypeEn())
                                    .fr(module.getModuleType().getModuleTypeFr())
                                    .build())
                            .semesters(module.getSemesters())
                            .specialization(SpecializationDto.builder()
                                    .de(module.getSpecialization().getSpecializationDe())
                                    .en(module.getSpecialization().getSpecializationEn())
                                    .fr(module.getSpecialization().getSpecializationFr())
                                    .build())
                            .build();
                })
                .distinct()
                .toList();
    }


        @Override
        public List<ModuleDto> getModulesByCode (String code){
            log.atInfo().log("Getting modules by code: {}", code);
            List<Module> results = this.moduleRepository.findModulesByCode(code, Sort.by("name"));
            return results
                    .stream()
                    .map(module ->
                            ModuleDto.builder()
                                    .code(module.getCode())
                                    .name(NameDto.builder()
                                            .de(module.getName().getNameDe())
                                            .build())
                                    .build()
                    )
                    .distinct()
                    .sorted(Comparator.comparingDouble(ModuleDto::getScore).reversed())
                    .toList();
        }

        @Override
        public ModuleDto getModuleByCode (String code){
            log.atInfo().log("Getting module by code: {}", code);
            Module result = this.moduleRepository.findModuleByCode(code).orElse(null);
            return ModuleDto.builder()
                    .code(result.getCode())
                    .name(NameDto.builder()
                            .de(result.getName().getNameDe())
                            .build())
                    .build();
        }
    }
