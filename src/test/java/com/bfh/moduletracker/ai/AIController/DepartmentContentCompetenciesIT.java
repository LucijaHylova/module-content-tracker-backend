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
import com.bfh.moduletracker.ai.model.entity.DepartmentContent;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.repository.DepartmentContentRepository;
import com.bfh.moduletracker.ai.service.auth.JwtService;
import org.junit.jupiter.api.BeforeAll;
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
class DepartmentContentCompetenciesIT {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private DepartmentContentRepository departmentContentRepository;

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    private FakeUserEntityGenerator fakeUserEntityGenerator;

    @MockitoBean
    private AuthController authController;

    private static final double TOTAL_VALUE = 1.0;
    private static final double EXPECTED_MATCHES_COUNT = 7;
    private static final double TOLERANCE_COMPETENCIES_VALUE = 0.12;
    private static final long TOLERANCE_KEYWORDS = 2;
    private List<DepartmentContent> departmentContent;


    @BeforeAll
    void setup() throws Exception {

        mvc.perform(get("/ai/getDepartmentContentCompetenciesData"))
                .andExpect(status().isOk())
                .andReturn();
        departmentContent = departmentContentRepository.findAll();
    }

    @Test
    @Transactional
    void testAll() {
        assertThat(departmentContent)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Transactional
    void shouldValidateCompetenciesContentCategoryForWirtschaftUndManagementDepartment() {
        /**
         Original Module Competencies of  Wirtschaft Und Management Department:
         Studierende analysieren digitale Geschäftsmodelle und entwickeln eigene Strategien zur digitalen Wertschöpfung.
         Studierende analysieren Geschäftsdaten, erstellen Visualisierungen und leiten Handlungsempfehlungen ab.
         Studierende modellieren und analysieren Geschäftsprozesse und verstehen den Einsatz von ERP-Systemen im Unternehmenskontext.
         Studierende lernen, unternehmerische Daten systematisch zu analysieren und mithilfe von Data-Warehouse-Architekturen sowie BI-Tools entscheidungsrelevante Informationen aufzubereiten.
         Die Studierenden können IT-Projekte nach klassischen und agilen Ansätzen planen, koordinieren und im Team erfolgreich umsetzen.
         Studierende verstehen zentrale Konzepte der digitalen Transformation und analysieren Geschäftsmodelle digitaler Unternehmen.
         Studierende sind in der Lage, Geschäftsprozesse zu modellieren, digitale Optimierungspotenziale zu erkennen und diese durch Automatisierungstechniken umzusetzen.
         Die Studierenden verstehen Cloud-Architekturen und können Daten sicher und effizient in cloudbasierten Umgebungen verwalten.
         Studierende erkennen sicherheitsrelevante Risiken in Unternehmen und wenden geeignete Schutz- und Datenschutzmaßnahmen unter Berücksichtigung rechtlicher Rahmenbedingungen an.
         Die Studierenden entwerfen mobile Geschäftsanwendungen und verstehen die technischen sowie wirtschaftlichen Grundlagen mobiler Geschäftsmodelle.
         Studierende analysieren digitale Märkte und entwickeln Konzepte für erfolgreiche Online-Geschäftsmodelle unter Berücksichtigung technologischer und rechtlicher Aspekte.
         Die Studierenden reflektieren den ethischen Umgang mit Daten und Algorithmen und können verantwortungsvolle Handlungsstrategien im digitalen Kontext entwickeln.
         **/

        String department = "Wirtschaft und Management";
        double averageValue = TOTAL_VALUE / EXPECTED_MATCHES_COUNT;

        List<String> expectedKeywords = List.of(
                "geschäftsmodelle analysieren",
                "strategien entwickeln",
                "geschäftsdaten visualisieren",
                "prozesse modellieren",
                "erp-systeme verstehen",
                "daten systematisieren",
                "informationen aufbereiten",
                "bi-tools einsetzen",
                "it-projekte planen",
                "it-projekte koordinieren",
                "projekte planen",
                "projekte koordinieren",
                "teams organisieren",
                "cloud-architekturen verstehen",
                "daten verwalten",
                "daten absichern",
                "daten visualisieren",
                "datenschutzmassnahmen anwenden",
                "mobile anwendungen entwerfen",
                "geschäftsmodelle gestalten",
                "digitale markte analysieren",
                "märkte analysieren",
                "online-konzepte entwickeln",
                "recht berücksichtigen",
                "algorithmen reflektieren",
                "ethik berücksichtigen",
                "optimierungspotenziale erkennen",
                "automatisierungstechniken umsetzen",
                "prozesse automatisieren",
                "daten transformieren",
                "entscheidungen unterstützen",
                "konzepte für online-geschäftsmodelle entwickeln",
                "unternehmensdaten analysieren",
                "handlungsstrategien entwickeln",
                "technologien bewerten",
                "sicherheitsrelevante risiken erkennen",
                "sicherheitsrisiken erkennen"
        );


        Optional<DepartmentContent> programContent = departmentContentRepository.findByDepartment(department);

        assertThat(programContent).isPresent();

        List<String> keys = programContent.get().getCompetencies().stream()
                .map(LocalizedVisualDataEntry::getKey_de)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(EXPECTED_MATCHES_COUNT - TOLERANCE_KEYWORDS);

        programContent.get().getCompetencies().forEach(requirement -> {
            double value = requirement.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_COMPETENCIES_VALUE, averageValue + TOLERANCE_COMPETENCIES_VALUE);
        });
    }
}
