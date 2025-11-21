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
class ModuleContentShortDescriptionIT {

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
    private static final double TOLERANCE_SHORT_DESCRIPTION_VALUE = 0.12;
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
    void shouldValidateShortDescriptionContentCategoryForModuleBSS1203_50() {
        /**
         Original Module Short Description:
         Sprache ist ein mächtiges Werkzeug, das unsere Beziehungen zu Adressat*innen, Kolleg*innen und der Gesellschaft prägt.
         Wie können wir dieses Werkzeug nutzen, um mehr Inklusion, Teilhabe und Chancengleichheit zu erzielen und dadurch der gesellschaftlichen Spaltung entgegenzuwirken?
         In diesem Modul lernen Sie, wie Sie die Grundlagen barrierefreier und inklusiver Kommunikation verstehen und anwenden können.
         Der Fokus liegt einerseits auf der Sensibilisierung für die verschiedenen Anspruchsgruppen und deren Bedürfnisse bezüglich (alternativer) Kommunikationsformen.
         Andererseits liegt der Fokus auf der konkreten Vermittlung von Techniken und Methoden, die eine inklusive und zugängliche Kommunikation ermöglichen:
         Neben Zugängen durch Leichte(re) Sprache werden spezifische Kommunikationsformen, z.B. für Menschen mit Autismus, thematisiert und entsprechende Techniken erlernt.
         Durch praktische Übungen werden Sie befähigt, barrierefreie und inklusive Kommunikationsstrategien in unterschiedlichen Kontexten erfolgreich umzusetzen.
         Dabei setzen Sie sich auch kritisch mit Ihrer professionellen Identität als Fachpersonen der Sozialen Arbeit auseinander.
         **/

        String code = "BSS1203.50";
        double expectedKeywordCount = 4.8;
        double averageValue = TOTAL_VALUE / expectedKeywordCount;

        List<String> expectedKeywords = List.of(
                "sprache",
                "sprache als werkzeug",
                "sprache als mächtiges werkzeug",
                "sprache als werkzeug für inklusion, teilhabe und chancengleichheit",
                "sprache als werkzeug für inklusion",
                "sprache als werkzeug für teilhabe",
                "sprache als werkzeug für chancengleichheit",
                "teilhabe",
                "chancengleichheit",
                "inklusion",
                "teilhabe und chancengleichheit",
                "inklusion",
                "teilhabe",
                "inklusion und teilhabe",
                "inklusion und chancengleichheit",
                "gesellschaftliche spaltung",
                "alternative kommunikationsformen",
                "vermittlung von techniken und methoden",
                "vermittlung von techniken",
                "vermittlung von methoden",
                "spezifische kommunikationsformen",
                "menschen mit autismus",
                "kommunikationsformen",
                "kommunikation",
                "techniken und methoden für inklusive kommunikation",
                "techniken und methoden für kommunikation",
                "inklusive kommunikation",
                "barrierefreie kommunikation",
                "kommunikationsformen für menschen mit autismus",
                "menschen mit autismus",
                "barrierefreie kommunikationsstrategien",
                "inklusive kommunikationsstrategien",
                "barrierefreie strategien",
                "inklusiven strategien",
                "spezifische kommunikationsformen",
                "sensibilisierung für anspruchsgruppen"
        );


        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getShortDescription().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedKeywordCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getShortDescription().forEach(shortDescription -> {
            double value = shortDescription.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_SHORT_DESCRIPTION_VALUE, averageValue + TOLERANCE_SHORT_DESCRIPTION_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateShortDescriptionContentCategoryForModuleBTI1021() {
        /**
         Original Module Short Description:
         Dieses Modul führt die Studierenden in die Grundlagen der Informatik ein. Rechnerarchitekturen, Maschinensprachen und
         hardwarenahe Softwareentwicklung in C und Assembler stehen im Vordergrund. Die verschiedenen Darstellungsformen von Zahlen und Zeichenketten werden diskutiert und
         praktisch erprobt. Weiterhin wird das Verständnis für undefiniertes Verhalten in Software geweckt und die Fähigkeit zum maschinennahen Debuggen einfacher Programme gefördert.
         **/

        String code = "BTI1021";
        double expectedKeywordCount = 5.5;
        double averageValue = TOTAL_VALUE / expectedKeywordCount;

        List<String> expectedKeywords = List.of(
                "grundlagen der informatik",
                "rechnerarchitekturen",
                "maschinensprachen",
                "hardwarenahe softwareentwicklung",
                "c und assembler",
                "hardwarenahe softwareentwicklung in c und assembler",
                "softwareentwicklung in c",
                "softwareentwicklung in assembler",
                "softwareentwicklung in c und assembler",
                "darstellungsformen von zahlen",
                "darstellungsformen von zeichenketten",
                "darstellungsformen von zahlen und zeichenketten",
                "zahlen und zeichenketten",
                "undefiniertes verhalten",
                "undefiniertes verhalten in software",
                "debuggen einfacher programme"
        );


        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getShortDescription().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedKeywordCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getShortDescription().forEach(shortDescription -> {
            double value = shortDescription.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_SHORT_DESCRIPTION_VALUE, averageValue + TOLERANCE_SHORT_DESCRIPTION_VALUE);
        });
    }

    @Test
    @Transactional
    void shouldValidateShortDescriptionContentCategoryForModuleBTI1311() {
        /**
         Original Module Short Description:
         unknown
         **/
        String code = "BTI1311";

        double expectedKeywordCount = 3.5;
        double averageValue = TOTAL_VALUE / expectedKeywordCount;

        List<String> expectedKeywords = List.of(
                "relationale datenbank",
                "relationale datenbanken",
                "nicht relationale datenbanken",
                "relationales datenbankmodell",
                "relationale datenbankmodelle",
                "nicht relationale datenbankmodelle",
                "nicht relationale datenbankmodell",
                "datenbanksysteme",
                "datenbanken",
                "datenbank",
                "datenbank-management",
                "datenbankmanagement",
                "relationale datenbank-management-systeme",
                "jdbc",
                "java-programme mit datenbankanbindung",
                "datenbankmodelle",
                "datenbankmodellierung",
                "semantische datenmodellierung",
                "semantisches datenmodell",
                "datenbank-abfragen",
                "datenbank-manipulation"
        );

        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent).isPresent();

        List<String> keys = moduleContent.get().getShortDescription().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();
        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(expectedKeywordCount - TOLERANCE_KEYWORDS);

        moduleContent.get().getShortDescription().forEach(shortDescription -> {
            double value = shortDescription.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_SHORT_DESCRIPTION_VALUE, averageValue + TOLERANCE_SHORT_DESCRIPTION_VALUE);
        });
    }
}
