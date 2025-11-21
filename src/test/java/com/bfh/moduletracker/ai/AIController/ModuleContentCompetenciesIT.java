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
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.model.entity.ModuleContent;
import com.bfh.moduletracker.ai.repository.ModuleContentRepository;
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
class ModuleContentCompetenciesIT {

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
    private static final double TOLERANCE_COMPETENCIES_VALUE = 0.15;
    private static final double TOLERANCE_KEYWORDS = 2.5;
    private List<ModuleContent> moduleContents;


    @BeforeAll
    void setup() throws Exception {

        mvc.perform(get("/ai/getModuleContentData"))
                .andExpect(status().isOk())
                .andReturn();
        moduleContents = moduleContentRepository.findAll();
    }

    @Test
    @Transactional
    void testAll() {
        System.out.println(moduleContents.stream().sorted((s1, s2) -> s1.getCode().compareTo(s2.getCode())));
        assertThat(moduleContents)
                .isNotEmpty()
                .hasSize(40);
    }

    @Test
    @Transactional
    void shouldValidateCompetenciesContentCategoryForModuleBTI1021() {
        /**
         Original Module Competencies:
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:
         1. Wissen und VerstehenDie Studierenden verstehen die Grundlagen von Rechnerarchitekturen und die Prinzipien von Maschinensprachen.Sie wissen, was ein Compiler ist und
         kennen die Sprachkonstrukte der Programmiersprache C.Sie kennen die wichtigsten Entwicklungswerkzeuge für C- und Assembler-Projekte.
         2. Anwenden und AnalyseDie Studierenden verstehen, wie Zahlen und Zeichenketten in Computern dargestellt werden und können zwischen den verschiedenen Darstellungsformen konvertieren.
         Sie können einfache Programme in Maschinensprache lesen und verstehen.Sie können selbstständig einfache Programme in C entwickeln.
         3. Synthese und BeurteilenDie Studierenden verstehen die Bedeutung und die wichtigsten Ursachen für undefiniertes Verhalten von Software.Sie sind in der Lage,einfache Programme zu debuggen.
         **/

        String code = "BTI1021";
        double expectedMatchesCount = 6.0;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "grundlagen von rechnerarchitekturen verstehen",
                "grundlagen von rechnerarchitekturen kennen",
                "rechnerarchitekturen verstehen",
                "prinzipien von maschinensprachen kennen",
                "prinzipien von maschinensprachen verstehen",
                "maschinensprachen verstehen",
                "compiler verstehen",
                "sprachkonstrukte der programmiersprache c kennen",
                "programmiersprache c verstehen",
                "c- und assembler-projekte kennen",
                "c-projekte kennen",
                "assembler-projekte kennen",
                "entwicklungswerkzeuge für c- und assembler-projekte kennen",
                "einfache programme in c entwickeln",
                "programme in c entwickeln",
                "c-programe entwickeln",
                "zahlen und zeichenketten darstellen",
                "zahlen und zeichenketten in computern darstellen",
                "zahlen und zeichenketten konvertieren",
                "maschinensprache kennen",
                "maschinensprache lesen",
                "maschinensprache verstehen",
                "maschinensprache lesen und verstehen",
                "programme in c entwickeln",
                "programme entwickeln",
                "undefiniertes verhalten von software verstehen",
                "einfache programme debuggen",
                "programme debuggen"
        );


        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getCompetencies().stream()
                .map(LocalizedVisualDataEntry::getKey_de)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedMatchesCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getCompetencies().forEach(requirement -> {
            double value = requirement.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_COMPETENCIES_VALUE, averageValue + TOLERANCE_COMPETENCIES_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateCompetenciesContentCategoryForModuleBTI1301() {
        /**
         Original Module Competencies:
         1. Wissen und Verstehen- Sie kennen die Funktionsweise von HTTP und verstehen den Aufbau einer Webanwendung.
         - Sie verstehen die Prinzipien einer REST-Schnitstelle.
         - Sie kennen JavaScript und wissen, wie es im Browser verwendet wird.
         2. Anwenden und Analyse- Sie können eine REST-Schnitstelle entwerfen und implementieren.
         - Sie sind imstande, mit JavaScript eine Single-Page-Applikation zu implementieren.
         3. Urteilen und Probleme bearbeiten- Sie können die verschiedenen Aspekte einer Webanwendung analysieren.
         - Sie können unterschiedliche Architekturen von Webanwendungen beurteilen.
         **/

        String code = "BTI1301";
        double expectedMatchesCount = 5.0;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "webanwendung verstehen",
                "webanwendung beurteilen",
                "webanwendung analysieren",
                "funktionsweise von http verstehen",
                "javascript kennen",
                "javascript verstehen",
                "javascript im browser verwenden",
                "http funktionsweise verstehen",
                "rest-schnittstelle entwerfen",
                "rest-schnittstelle implementieren",
                "rest-schnittstelle entwerfen und implementieren",
                "single-page-applikation implementieren",
                "aspekte einer webanwendung analysieren",
                "architekturen von webanwendungen beurteilen",
                "urteilen und probleme bearbeiten"

        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getCompetencies().stream()
                .map(LocalizedVisualDataEntry::getKey_de)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedMatchesCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getCompetencies().forEach(competency -> {
            double value = competency.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_COMPETENCIES_VALUE, averageValue + TOLERANCE_COMPETENCIES_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateCompetenciesContentCategoryForModuleBTI1311() {
        /**
         Original Module Competencies:
         Am Ende dieses Moduls haben die Studierenden folgende Fähigkeiten erworben:
         Wissen und Verstehen

         die Zielsetzung der semantische Datenmodellierung und die Konzepte des Entity-Relationship Modells;
         die grundlegenden Konzepte relationaler Datenbanken und der relationalen Algebra;
         die Konzepte zur Umsetzung eines Datenbank-Designs in eine relationale Datenbank;
         die Abfrage und Manipulation relationaler Datenbanken;
         die Konzepte und Funktionen der Java Database Connectivity (JDBC);
         die verschiedenen Konzepte und Arten aktiver Datenbank-Komponenten.


         Anwenden von Wissen und Verstehen

         können  für eine gegebene Problemstellung eine semantische  Informationsstruktur  mit Hilfe des Entity-Relationship Modells  modellieren;
         sind in der Lage die Informationsstruktur in eine relationale Datenbank umzusetzen;
         können eigene Java-Programme mit Datenbank-Anbindung realisieren;
         können eigene aktive Datenbank-Komponenten implementieren und diese ebenfalls in Java-Programme integrieren;
         können JDBC anwenden und eigene Java-Programme mit persistenten Daten realisieren.


         Urteilen und Probleme bearbeiten

         können  für gegebene Problemstellungen im Bereich des Entwurfs, der   Implementierung sowie der Anwendung relationaler Datenbanken   verschiedene Lösungsmöglichkeiten beurteilen sowie eigene
         Lösungskonzepte erstellen und implementieren.
         **/

        String code = "BTI1311";
        double expectedMatchesCount = 5.0;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "entity-relationship modells modellieren",
                "relationaler datenbanken umestzen",
                "relationale datenbanken",
                "datenbank-anbindung realisieren",
                "relationale datenbank abfragen",
                "datenbanken verstehen",
                "datenbank und relationale algebra verstehen",
                "datenbank-komponenten implementieren",
                "datenbank-design umsetzen",
                "informationsstruktur in relationale datenbank umsetzen",
                "java database connectivity",
                "database connectivity",
                "relationale datenbank anwenden",
                "jdbc anwenden",
                "jdbc verstehen",
                "relationale algebra verstehen",
                "relationale datenbanken umsetzen",
                "datenbank-komponenten implementieren",
                "datenmodellierung wissen",
                "datenmodellierung verstehen",
                "java-programme mit datenbank-anbindung realisieren",
                "java-programme realisieren",
                "java-programme integrieren",
                "java-programme realisieren",
                "lösungsmöglichkeiten beurteilen",
                "lösungskonzepte erstellen",
                "lösungskonzepte"
        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getCompetencies().stream()
                .map(LocalizedVisualDataEntry::getKey_de)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedMatchesCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getCompetencies().forEach(competencies -> {
            double value = competencies.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_COMPETENCIES_VALUE, averageValue + TOLERANCE_COMPETENCIES_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateCompetenciesContentCategoryForModuleBSS3204() {
        /**
         Original Module Competencies:
         unknown
         **/
        String code = "BSS3204";

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getCompetencies().stream()
                .map(LocalizedVisualDataEntry::getKey_de)
                .map(String::toLowerCase)
                .toList();

        List<String> expectedKeywords = List.of(
                "risikomanagement",
                "risikomanagement verstehen",
                "projektmanagement verstehen",
                "zeitmanagement verstehen",
                "projektteams führen",
                "team führen",
                "projektmethodik anwenden",
                "projektplanung realisieren",
                "projektplanung entwickeln",
                "projekt umsetzen",
                "projekt realisieren",
                "projektideen umsetzen",
                "projektideen realisieren",
                "projektideen entwickeln",
                "projektideen umsetzen",
                "projektmethodik anwenden",
                "projektideen ausarbeiten",
                "projektumsetzungsplan erstellen",
                "unknown",
                "keine"
        );

        boolean validRequirements = keys.isEmpty() || keys.stream().anyMatch(expectedKeywords::contains);

        assertThat(validRequirements)
                .isTrue();
    }
}
