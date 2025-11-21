package com.bfh.moduletracker.ai.AIController;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.bfh.moduletracker.ai.common.FakeUserEntityGenerator;
import com.bfh.moduletracker.ai.config.PostgresTestcontainerConfiguration;
import com.bfh.moduletracker.ai.config.TestSecurityConfiguration;
import com.bfh.moduletracker.ai.controller.AuthController;
import com.bfh.moduletracker.ai.model.entity.ModuleContent;
import com.bfh.moduletracker.ai.model.entity.VisualDataEntry;
import com.bfh.moduletracker.ai.repository.ModuleContentRepository;
import com.bfh.moduletracker.ai.service.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import({PostgresTestcontainerConfiguration.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("inttest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModuleContentRequirementsIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModuleContentRepository moduleContentRepository;

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    private FakeUserEntityGenerator fakeUserEntityGenerator;

    @MockitoBean
    private AuthController authController;

    private static final double TOTAL_VALUE = 1.0;
    private static final double TOLERANCE_REQUIREMENTS_VALUE = 0.15;
    private static final long TOLERANCE_KEYWORDS = 2;
    private List<ModuleContent> moduleContents;

    @BeforeEach
    void setup() throws Exception {
        mvc.perform(get("/ai/getModuleContentData"))
                .andExpect(status().isOk())
                .andReturn();

        moduleContents = moduleContentRepository.findAll();
    }

    @Test
    @Transactional
    void testAll() {

        assertThat(moduleContents)
                .isNotEmpty()
                .hasSize(40);
    }

    @Test
    @Transactional
    void shouldValidateRequirementsContentCategoryForModuleBTI1321() {
        /**
         Original Module Requirements:
         Elementare ZahlentheorieGrundlagen in NetzwerktechnologienGrundkenntnisse Architekturen von IT-Systemen
         **/
        String code = "BTI1321";
        double expectedMatchesCount = 3.5;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "elementare zahlentheorie",
                "elementare zahlentheorie grundlagen",
                "elementare zahlentheoriegrundlagen",
                "elementare zahlentheorie",
                "zahlentheorie",
                "zahlentheorie grundlagen",
                "grundlagen in zahlentheorie",
                "zahlentheoriegrundlagen",
                "netzwerke",
                "grundlagen in netzwerktechnologien",
                "netzwerktechnologien",
                "netzwerktechnologien grundkenntnisse",
                "netzwerktechnologiegrundkennstnisse",
                "it-architekturen",
                "architekturen von it-systemen");

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getRequirements().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();


        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedMatchesCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getRequirements().forEach(requirement -> {
            double value = requirement.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_REQUIREMENTS_VALUE, averageValue + TOLERANCE_REQUIREMENTS_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateRequirementsContentCategoryForModuleBTE5425() {
        /**
         Original Module Requirements:
         Grundkenntnisse der Mathematik, Physik, Chemie, Elektrotechnik, Elektronik und Mechatronik.
         /* *
         *
         */
        String code = "BTE5425";
        double expectedMatchesCount = 3.2;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "mathematik",
                "physik",
                "chemie",
                "elektrotechnik",
                "elektronik",
                "mechatronik",
                "grundkenntnisse der mathematik",
                "grundkenntnisse mathematik",
                "grundkenntnisse physik",
                "grundkenntnisse der physik",
                "grundkenntnisse chemie",
                "grundkenntnisse der chemie",
                "grundkenntnisse elektrotechnik",
                "grundkenntnisse der elektrotechnik",
                "grundkenntnisse elektronik",
                "grundkenntnisse der elektronik",
                "grundkenntnisse mechatronik",
                "grundkenntnisse der mechatronik"
        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getRequirements().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedMatchesCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getRequirements().forEach(requirement -> {
            double value = requirement.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_REQUIREMENTS_VALUE, averageValue + TOLERANCE_REQUIREMENTS_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateRequirementsContentCategoryForModuleBSS1203_50() {
        /**
         Original Module Requirements:
         KeinePriorität haben Studierende ab Studienphase 2
         **/
        String code = "BSS1203.50";

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getRequirements().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        List<String> expectedKeywords = List.of(
                "keine",
                "kein",
                "nicht notwendig",
                "nicht erforderlich",
                "nicht nötig",
                "unknown",
                "studienphase",
                "studienphase 2",
                "priorität haben studierende ab studienphase 2",
                "studierende ab studienphase 2"
        );

        boolean validRequirements = keys.isEmpty() || keys.stream().anyMatch(expectedKeywords::contains);


        assertThat(validRequirements)
                .isTrue();
    }

    @Test
    @Transactional
    void shouldValidateRequirementsContentCategoryForModuleBTI1021() {
        /**
         Original Module Requirements:
         Keine
         **/

        String code = "BTI1021";

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getRequirements().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        List<String> expectedKeywords = List.of(
                "keine",
                "kein",
                "nicht notwendig",
                "nicht erforderlich",
                "nicht nötig",
                "unknown"
        );

        boolean validRequirements = keys.isEmpty() || keys.stream().anyMatch(expectedKeywords::contains);

        assertThat(validRequirements)
                .isTrue();
    }
}
