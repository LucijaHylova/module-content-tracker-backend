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
class ModuleContentContentIT {

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
    private static final double TOLERANCE_CONTENT_VALUE = 0.12;
    private static final long TOLERANCE_KEYWORDS = 2;
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

        assertThat(moduleContents)
                .isNotEmpty()
                .hasSize(40);
    }

    @Test
    @Transactional
    void shouldValidateContentContentCategoryForModuleBTI1331() {
        /** Original Module Content:
         In diesem Kurs werden die folgenden Themen und Inhalte behandelt:Herkunft, Anwendung und Ausdehnung von Computer NetwerkenGrundbegriffe,
         Schichtenmodelle, Standardisierungs- und Normierungs-GremienBasistechnologien und -methodenÜbertragungsmedien,
         Bandbreite und Datenrate, Multiplex und Modulation, LatenzFehlererkennung und -korrekturLANs und WLANs, Bridging und
         SwitchingInternet Protokoll Familie:
         Internet Protokolle IP
         Adressierung, Routing
         Fehlerbehandlung (ICMP)
         Adressauflösung, Neighbor Discovery
          End-zu-end Kommunikation:
         Verbindungslos mit UDP, Multicast
         Verbindungsorientiert mit TCP
         Neue Möglichkeiten der End-zu-end Kommunikation (QUIC)
          Konzept der SocketsAnwendungsdienste (DNS, DHCP, Directories)Einführung in die Absicherung von Kanälen (TLS, SSH)
         Anwendungsprotokolle (HTTP, SMTP)HTTP/S basierte Kommunikation von Webservices, Websockets
         **/

        String code = "BTI1331";
        double expectedMatchesCount = 5.0;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "netwerkengrundbegriffe",
                "computer netwerkengrundbegriffe",
                "grundbegriffe",
                "netwerke",
                "netzwerk",
                "computer netzwerke",
                "computer netzwerk",
                "übertragungsmedien",
                "latenzFehlererkennung",
                "latenzFehlererkennung und -korrekturLANs",
                "wlans",
                "wlan",
                "bridging und SwitchingInternet Protokoll",
                "bridging",
                "switchinginternet Protokoll",
                "switching",
                "switching internet",
                "internet protokolle ip",
                "internet protokolle",
                "internet protokoll",
                "ip-protokolle",
                "ip",
                "adressierung und routing",
                "adressierung",
                "routing",
                "Fehlerbehandlung",
                "icmp",
                "neighbor discovery",
                "end-zu-end kommunikation",
                "udp",
                "multicast",
                "tcp",
                "quic",
                "socketsanwendungsdienste",
                "sockets",
                "anwendungsdienste",
                "webservices",
                "websockets",
                "anwendungsprotokolle",
                "http",
                "smtp"
        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getContent().stream()
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

        moduleContent.get().getContent().forEach(content -> {
            double value = content.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_CONTENT_VALUE, averageValue + TOLERANCE_CONTENT_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateContentContentCategoryForModuleBTI1311() {
        /** Original Module Content:
         In diesem Kurs werden die folgenden Themen und Inhalte behandelt:
         Grundlagen, Begriffe und Konzepte von Datenbanksystemen
         Semantische Datenmodellierung

         UML-Klassenmodell
         Entity-Relationship-Modell


         Relationales Datenbankmodell
         Funktionale Abhängigkeiten und Normalisierung
         Datenbank-Abfragen und -Manipulationen: SQL - Data Manipulation Language
         Datenbankschema-Definition: Structured Query Language (SQL) - Data Definition Language
         Programmatische Persistenz mit JDBC, "impedance mismatch"
         Externe Datensicht: Views
         Transaktionen, Locking
         Aktive Datenbank-Komponenten: Stored Procedures, Trigger, Functions
         **/

        String code = "BTI1311";
        double expectedMatchesCount = 6.5;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "datenbanksystemen",
                "datenbanksysteme",
                "grundlagen, begriffe und konzepte von datenbanksystemen",
                "konzepte von datenbanksystemen",
                "grundlagen von datenbanksystemen",
                "begriffe von datenbanksystemen",
                "semantische datenmodellierung",
                "uml-klassenmodell",
                "entity-relationship-modell",
                "relationales datenbankmodell",
                "funktionale abhängigkeiten",
                "funktionale abhängigkeit und normalisierung",
                "normalisierung",
                "latenzfehlererkennung und -korrekturlans",
                "datenbank-abfragen",
                "datenbank-abfragen und -manipulationen",
                "datenbankschema definieren",
                "data definition language",
                "data manipulation language",
                "sql-data manipulation language",
                "sql",
                "sql-abfragen",
                "programmatische persistenz",
                "programmatische persistenz mit jdbc",
                "jdbc",
                "externe datensicht",
                "transaktionen",
                "locking",
                "aktive datenbank-komponenten",
                "stored procedures, trigger, functions",
                "stored procedures"
        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getContent().stream()
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

        moduleContent.get().getContent().forEach(content -> {
            double value = content.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_CONTENT_VALUE, averageValue + TOLERANCE_CONTENT_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateContentContentCategoryForModuleWI104() {
        /**
         Original Module Content:
         Einführung in Projektmanagementmethoden mit Fokus auf agile Vorgehensweisen wie Scrum, Kanban und hybride Modelle.
         Praktische Anwendungen und Tools werden behandelt.
         **/

        String code = "WI104";
        double expectedMatchesCount = 4.8;
        double averageValue = TOTAL_VALUE / expectedMatchesCount;

        List<String> expectedKeywords = List.of(
                "projektmanagementmethoden",
                "einführung in projektmanagementmethoden",
                "projektmanagement",
                "scrum",
                "kanban",
                "scrum und kanban",
                "anwendungen und tools",
                "agile vorgehensweisen",
                "praktische anwendungen",
                "praktische tools",
                "einführung in agile vorgehensweisen",
                "hybride modelle",
                "praktische anwendungen und tools"
        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getContent().stream()
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

        moduleContent.get().getContent().forEach(content -> {
            double value = content.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_CONTENT_VALUE, averageValue + TOLERANCE_CONTENT_VALUE);
        });
    }
}
