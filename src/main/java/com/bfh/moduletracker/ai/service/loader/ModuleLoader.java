package com.bfh.moduletracker.ai.service.loader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bfh.moduletracker.ai.model.entity.AssessmentProportion;
import com.bfh.moduletracker.ai.model.entity.Competencies;
import com.bfh.moduletracker.ai.model.entity.ContactLessons;
import com.bfh.moduletracker.ai.model.entity.Content;
import com.bfh.moduletracker.ai.model.entity.Department;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.ModuleType;
import com.bfh.moduletracker.ai.model.entity.Name;
import com.bfh.moduletracker.ai.model.entity.Program;
import com.bfh.moduletracker.ai.model.entity.ProofCompetence;
import com.bfh.moduletracker.ai.model.entity.Requirements;
import com.bfh.moduletracker.ai.model.entity.Responsibility;
import com.bfh.moduletracker.ai.model.entity.SelfStudy;
import com.bfh.moduletracker.ai.model.entity.ShortDescription;
import com.bfh.moduletracker.ai.model.entity.Specialization;
import com.bfh.moduletracker.ai.model.entity.TeachingForm;
import com.bfh.moduletracker.ai.model.entity.TeachingMethod;
import com.bfh.moduletracker.ai.model.entity.Workload;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Profile("local")
public class ModuleLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ModuleLoader.class);

    private final ModuleReader moduleReader;
    private final ModuleRepository moduleRepository;


    public ModuleLoader(ModuleReader moduleReader, ModuleRepository repository) {
        this.moduleReader = moduleReader;
        this.moduleRepository = repository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
     load();
    }

    @Transactional
    public void load() throws JsonProcessingException {
       load(this.moduleReader.read());
    }


    @Transactional
    public void load(List<Document> documents) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        for (Document document : documents) {
            Map<String, Object> metaData = document.getMetadata();
            Module.ModuleBuilder builder = Module.builder();
//
//            String queryMode = (String) metaData.get("query_mode");
//            if (queryMode == null || !queryMode.trim().equals("TZ")) {
//                log.debug("Skipping module {} due to query_mode={}", metaData.get("code"), queryMode);
//                continue;
//            }
//            builder.queryMode(queryMode.trim());

            for (String key : moduleReader.getHeaderOrder()) {
                String value = (String) metaData.getOrDefault(key, "unknown");

                switch (key) {
                    case "year" -> builder.year(value);
                    case "query_mode" -> builder.queryMode(value);
                    case "department" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.department(Department.builder()
                                    .departmentDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .departmentEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .departmentFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.department(Department.builder().departmentDe(value).build());
                        }
                    }
                    case "program" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.program(Program.builder()
                                    .programDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .programEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .programFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.program(Program.builder().programDe(value).build());
                        }
                    }
                    case "name" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.name(Name.builder()
                                    .nameDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .nameEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .nameFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.name(Name.builder().nameDe(value).build());
                        }
                    }
                    case "code" -> builder.code(value);
                    case "ects" -> builder.ects(parse(value));
                    case "module_type" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.moduleType(ModuleType.builder()
                                    .moduleTypeDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .moduleTypeEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .moduleTypeFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.responsibility(Responsibility.builder().responsibilityDe(value).build());
                        }
                    }
                    case "responsibility" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.responsibility(Responsibility.builder()
                                    .responsibilityDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .responsibilityEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .responsibilityFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.responsibility(Responsibility.builder().responsibilityDe(value).build());
                        }
                    }
                    case "short_description" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.shortDescription(ShortDescription.builder()
                                    .shortDescriptionDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .shortDescriptionEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .shortDescriptionFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.shortDescription(ShortDescription.builder()
                                    .shortDescriptionDe(value)
                                    .build());
                        }
                    }
                    case "content" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.content(Content.builder()
                                    .contentDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .contentEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .contentFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.content(Content.builder().contentDe(value).build());
                        }
                    }
                    case "teaching_form" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.teachingForm(TeachingForm.builder()
                                    .teachingFormDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .teachingFormEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .teachingFormFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.teachingForm(TeachingForm.builder().teachingFormDe(value).build());
                        }
                    }
                    case "teaching_method" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.teachingMethod(TeachingMethod.builder()
                                    .teachingMethodDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .teachingMethodEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .teachingMethodFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.teachingMethod(TeachingMethod.builder().teachingMethodDe(value).build());
                        }
                    }
                    case "workload" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.workload(Workload.builder()
                                    .workloadDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .workloadEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .workloadFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.workload(Workload.builder().workloadDe(value).build());
                        }
                    }
                    case "contact_lessons" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.contactLessons(ContactLessons.builder()
                                    .contactLessonDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .contactLessonEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .contactLessonFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.contactLessons(ContactLessons.builder().contactLessonDe(value).build());
                        }
                    }
                    case "self_study" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.selfStudy(SelfStudy.builder()
                                    .selfStudyDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .selfStudyEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .selfStudyFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.selfStudy(SelfStudy.builder().selfStudyDe(value).build());
                        }
                    }
                    case "requirements" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.requirements(Requirements.builder()
                                    .requirementsDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .requirementsEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .requirementsFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.requirements(Requirements.builder().requirementsDe(value).build());
                        }
                    }
                    case "proof_competence" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.proofCompetence(ProofCompetence.builder()
                                    .proofCompetenceDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .proofCompetenceEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .proofCompetenceFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.proofCompetence(ProofCompetence.builder().proofCompetenceDe(value).build());
                        }
                    }
                    case "assessment_proportion" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.assessmentProportion(AssessmentProportion.builder()
                                    .assessmentProportionDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .assessmentProportionEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .assessmentProportionFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.assessmentProportion(AssessmentProportion.builder().assessmentProportionDe(value).build());
                        }
                    }
                    case "competencies" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);
                            builder.competencies(Competencies.builder()
                                    .competenciesDe(node.has("de") ? node.get("de").asText() : "unknown")
                                    .competenciesEn(node.has("en") ? node.get("en").asText() : "unknown")
                                    .competenciesFr(node.has("fr") ? node.get("fr").asText() : "unknown")
                                    .build());
                        } else {
                            builder.competencies(Competencies.builder().competenciesDe(value).build());
                        }
                    }
                    case "semesters" -> {
                        List<Integer> semesters;

                        if (value == null || value.isBlank() || value.equalsIgnoreCase("unknown")) {

                            semesters = List.of(0);
                        } else {
                            semesters = Arrays.stream(value.split(","))
                                    .map(String::trim)
                                    .map(this::parse)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
                        }

                        builder.semesters(semesters);
                    }
                    case "languages" -> builder.languages(value).build();
                    case "created_at" -> builder.createdAt(value).build();
                    case "updated_at" -> builder.updatedAt(value).build();
                    case "hierarchy_4" -> {
                        if (isJSONValid(value)) {
                            JsonNode node = mapper.readTree(value);

                            builder.specialization(Specialization.builder()
                                    .specializationDe(extractSpecialization(node.has("de") ? node.get("de").asText() : "undefined"))
                                    .specializationEn(extractSpecialization(node.has("en") ? node.get("en").asText() : "undefined"))
                                    .specializationFr(extractSpecialization(node.has("fr") ? node.get("fr").asText() : "undefined"))
                                    .build());

                        } else {
                            builder.specialization(Specialization.builder()
                                    .specializationDe(extractSpecialization(value))
                                    .build());
                        }
                    }
                }
            }

            Module module = builder.build();
            moduleRepository.findById(module.getCode()).ifPresent(moduleRepository::delete);
            this.moduleRepository.save(module);
        }
        log.info("Modules loaded: {}", this.moduleRepository.count());
    }

    String extractSpecialization(String text) {
        if (text == null) return "no specialization";
        if (text.contains("Vertiefung")) {
            text = text.trim().replace("Vertiefung ", "");
            return text;
            }
        return "no specialization";
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String parse(String raw) {
        if (raw == null) return "0";

        raw = raw.trim();

        if (raw.isEmpty() || raw.equalsIgnoreCase("unknown")) {
            return "0";
        }

        try {
            raw = raw.trim().replace(',', '.');
            double numeric = Double.parseDouble(raw);
            return String.valueOf((int) Math.round(numeric));
        } catch (NumberFormatException e) {
            log.warn("Invalid numeric value '{}' in module import - defaulting to 0", raw);
            return "0";
        }
    }
}
