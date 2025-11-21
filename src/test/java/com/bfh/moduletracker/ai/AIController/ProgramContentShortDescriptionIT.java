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
import com.bfh.moduletracker.ai.model.entity.ProgramContent;
import com.bfh.moduletracker.ai.model.entity.VisualDataEntry;
import com.bfh.moduletracker.ai.repository.ProgramContentRepository;
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
class ProgramContentShortDescriptionIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProgramContentRepository programContentRepository;

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    private FakeUserEntityGenerator fakeUserEntityGenerator;

    @MockitoBean
    private AuthController authController;

    private static final double TOTAL_VALUE = 1.0;
    private static final double EXPECTED_MATCHES_COUNT = 6;
    private static final double TOLERANCE_COMPETENCIES_VALUE = 0.12;
    private static final long TOLERANCE_KEYWORDS = 2;
    private List<ProgramContent> programContent;


    @BeforeAll
    void setup() throws Exception {

        mvc.perform(get("/ai/getProgramContentShortDescriptionData"))
                .andExpect(status().isOk())
                .andReturn();
        programContent = programContentRepository.findAll();
    }

    @Test
    @Transactional
    void testAll() {
        assertThat(programContent)
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    @Transactional
    void shouldValidateShortDescriptionContentCategoryForInformatikProgram() {
        /**
         Original Module Short Description of Informatik Program:
         Spaces are products of social processes and power relations. Based on this postulate, this course invites students to explore the concept of "queer space", both as a theoretical framework and as a practical intervention. "Queer space" can be understood as a space that challenges and subverts oppressive power structures of (hetero-)normativity. Through readings and media, students will explore queer spaces and queer spatialities across various sociocultural as well as historical contexts, including New York City during the "AIDS crisis", post-colonial Mumbai or Berne before and around the time of the youth unrests of the 1980s. We will also engage with the notions and topics of performativity, gentrification and reclamation, migration, (sub-)urbanity, hetero- and homonormativity, intersectionality, violence, and futurity within the context of queer space. Through readings, walks and media interventions, you will develop your own critical and creative perspectives on queer space and how it can influence your social work practice in the future.
         In der Sozialen Arbeit sind Konflikte und Widerstände allgegenwärtig. Unabhängig von den Handlungsfeldern - sei es im eigenen Konfliktverhalten, in Klient*innen-Beziehungen, in Teams oder in der Gesamtorganisation - stellen sie eine ständige Herausforderung dar. Sie erfordern eine fundierte Auseinandersetzung, vielfältige Fähigkeiten sowie einen persönlichen Umgang mit ihnen.   Deshalb setzt das Modul auf eine Verschränkung systemischer Theorie und praktischen Erlebens mittels interaktiven Theaters unter Anleitung von Schauspieler*innen. Ziel des Moduls ist es einerseits, eine neue Sicht auf Widerstände und Konflikte zu entwickeln, ihre positiven Seiten zu stärken und ihr Transformationspotential zu entdecken. Andererseits wird spielerisch mit verschiedenen Übungen, persönlichen Fallbeispielen, Reflexion und Rollenspiel das Wesen von Konflikten und Widerständen ergründet und neue Handlungsalternativen praxisnah ge- und erprobt. 
         Sprache ist ein mächtiges Werkzeug, das unsere Beziehungen zu Adressat*innen, Kolleg*innen und der Gesellschaft prägt. Wie können wir dieses Werkzeug nutzen, um mehr Inklusion, Teilhabe und Chancengleichheit zu erzielen und dadurch der gesellschaftlichen Spaltung entgegenzuwirken?  In diesem Modul lernen Sie, wie Sie die Grundlagen barrierefreier und inklusiver Kommunikation verstehen und anwenden können. Der Fokus liegt einerseits auf der Sensibilisierung für die verschiedenen Anspruchsgruppen und deren Bedürfnisse bezüglich (alternativer) Kommunikationsformen. Andererseits liegt der Fokus auf der konkreten Vermittlung von Techniken und Methoden, die eine inklusive und zugängliche Kommunikation ermöglichen: Neben Zugängen durch Leichte(re) Sprache werden spezifische Kommunikationsformen, z.B. für Menschen mit Autismus, thematisiert und entsprechende Techniken erlernt.  Durch praktische Übungen werden Sie befähigt, barrierefreie und inklusive Kommunikationsstrategien in unterschiedlichen Kontexten erfolgreich umzusetzen. Dabei setzen Sie sich auch kritisch mit Ihrer professionellen Identität als Fachpersonen der Sozialen Arbeit auseinander.
         Das Modul legt die konzeptuellen und methodischen Grundlagen für die Durchführung eines integrativen, innovativen und partizipativen Projekts. Die Studierenden setzen sich dazu mit integrativer Projektmethodik sowie mit innovativen und partizipativen Ansätzen der Projektarbeit auseinander. Auch lernen sie das Lehrkonzept «Service Learning» kennen, welches darauf abzielt, im Rahmen studentischer Projektarbeiten ein gesellschaftliches Anliegen zu bearbeiten und damit die Verknüpfung von akademischem Lernen und zivilgesellschaftlichem Engagement ermöglicht. Darüber hinaus entwickeln die Studierenden in einem Projektteam von drei bis vier Studierenden ein Projekt. Gemeinsam im Austausch mit Projektpartner*innen arbeiten sie hierfür eine Projektidee zu einem Projektumsetzungsplan aus. Projektpartner*innen sind Einzelpersonen, Vereine, Netzwerke, soziale Bewegungen oder Fachorganisationen, ebenso wie die im Projekt adressierten Zielgruppen. Die Studierenden verschriftlichen den Umsetzungsplan und stellen ihn am Ende des Seminars im Rahmen eines Pitches den Mitstudierenden und weiteren Interessierten vor.   Im Modul «Praxisprojekt» oder alternativ im Modul «Bachelor-Thesis» kann bei Interesse die im Modul «Projektmanagement» ausgearbeitete Projektidee aufgegriffen werden.
         Dieses Modul hat zum Ziel, die Studierenden mit den wichtigsten Messprinzipien zur Messung nichtelektrischer Grössen vertraut zu machen und sie zu befähigen, Sensoren zu evaluieren und deren korrekten Einsatz zu beurteilen.  Dieses Modul wird zweisprachig (Deutsch, Französisch) unterrichtet. Die primären Unterrichtsunterlagen (Foliensätze), einschliesslich Projekt- und Aufgabenbeschreibungen, werden auf Deutsch und Französisch bereitgestellt. Sekundäre Unterlagen können nur auf Deutsch, Französisch oder Englisch vorliegen.
         unknown
         unknown
         unknown
         Die Studierenden lernen, kleine und mittlere Programme in der Programmiersprache Java zu entwerfen, mit einer Entwicklungsumgebung zu realisieren und auszuführen.
         Die Studierenden lernen, kleine und mittlere Programme in der Programmiersprache Kotlin zu entwerfen und zu realisieren sowie diese in einer Entwicklungsumgebung auszuführen.
         In diesem Modul lernen die Studierenden, objektorientierte Anwendungen mittlerer Grösse zu entwerfen und mit geeigneten Java-Technologien zu implementieren. Dies unter Verwendung von Funktionen für Mehrsprachigkeit, Dateizugriffe, Datenverarbeitung in XML- und JSON-Formaten sowie unter Verwendung von GUI-Technologien.
         In diesem Modul lernen die Studierenden, objektorientierte Anwendungen mittlerer Größe zu entwerfen und mit geeigneten Werkzeugen in Kotlin zu implementieren. Dazu werden sie in geeignete Konzepte und Prinzipien der objektorientierten und funktionalen Programmierung in Kotlin eingeführt, die es ihnen ermöglichen, auch komplexe Anwendungen effizient und sicher zu realisieren.
         Dieses Modul führt die Studierenden in die Grundlagen der Informatik ein. Rechnerarchitekturen, Maschinensprachen und hardwarenahe Softwareentwicklung in C und Assembler stehen im Vordergrund. Die verschiedenen Darstellungsformen von Zahlen und Zeichenketten werden diskutiert und praktisch erprobt. Weiterhin wird das Verständnis für undefiniertes Verhalten in Software geweckt und die Fähigkeit zum maschinennahen Debuggen einfacher Programme gefördert.
         Der Kurs behandelt grundlegende Themen und Methoden zur Erarbeitung und Validierung von benutzerfreundlichen Interaction Designs. Sie erhalten einen Einblick über die vielzähligen Aspekte, welche bei der Gestaltung der Mensch-Maschine-Schnittstelle relevant sind. Es werden Themen wie Usability, User Experience, Human-Centered Design, Wahrnehmung, Gestaltgesetze, Heuristiken, Accessibility, Kreativitätstechniken, Prototyping und Evaluation abgedeckt. Ausgewählte Methoden werden zudem im Praxisteil vertieft und angewandt.
         In diesem Modul werden die Studierenden mit den Definitionen und Begriffen des Anforderungsmanagements vertraut gemacht. Sie können die erfolgskritischen Rahmenbedingungen benennen und die Vor- und Nachteile verschiedener Ansätze des Anforderungsmanagements darstellen. Darüber hinaus werden sie in der korrekten Erfassung und Operationalisierung von Zielen und Vorgaben geschult, um ein adäquates Anforderungsmanagement einführen und Konfliktsituationen im Projektverlauf lösen zu können.
         Die Studierenden erwerben im Modul grundlegendes Wissen und Verständnis zu Software Engineering-Projekten, agiler und plan-gesteuerter Vorgehensweisen sowie gängigen Tools zur Projektabwicklung. Sie können Software-Systeme mit UML modellieren, kennen Entwurfsprinzipien und -muster und können Software Engineering-Prozesse entwerfen und umsetzen. Sie sind in der Lage, Entwurfsentscheidungen zu treffen, die Qualität einer Software zu sicherzustellen und zu bewerten und gut strukturierte, flexible und erweiterbare Systeme zu konzipieren, zu implementieren und zu dokumentieren.
         Das Modul IT-Controlling gibt zunächst einen Überblick über den wirtschaftlichen Einsatz der Informatik in Unternehmen sowie über gängige Geschäftsmodelle von Informatikunternehmen.  Anschliessend werden die Grundsätze des internen und externen Rechnungswesens eingeführt, welche für die Beschaffung und den Betrieb von Informatikmitteln in Unternehmen sowie für die Kalkulation von Informatikdienstleistungen von Bedeutung sind.  Schliesslich werden Grundlagen betriebswirtschaftlicher Methoden und Werkzeuge vermittelt und angewendet, mit denen die Studierenden betriebliche Probleme mit IT-Unterstützung lösen können.  Damit erwerben die Studierenden wichtiges Know-how für den wirtschaftlichen Erfolg mit Informatik in der Praxis, für sich selbst und für ihre Kunden.
         Nach Abschluss des Moduls haben die Studierenden das Wissen erworben, um die Voraussetzungen für gültige Verträge zu identifizieren und Risiken bei der Vertragsentstehung und -abwicklung grob zu bewerten. Sie können einfache Probleme bei der Auslegung von Software-spezifischen Verträgen analysieren und verstehen das Konzept der Haftungsgrundlagen. Darüber hinaus sind sie in der Lage, rechtliche Fragen / Probleme zu erkennen und grobe Beurteilungen geschäftsbezogener Problemstellungen rechtzeitig vorzunehmen.
         Dieses Modul führt die Studierenden in die Funktionsweise von HTTP, die verschiedenen Aspekte einer Webanwendung sowie die Entwicklung von REST-Schnittstellen und JavaScript-Clients ein.
         unknown
         Im Modul erwerben die Studierenden grundlegendes Wissen und Verständnis über symmetrische und asymmetrische Kryptographie, PKI und Authentisierungsverfahren. Sie entwickeln die Fähigkeit zur Anwendung und Analyse von X.509 Certification Authority, S/MIME und SSL/TLS-unterstützenden Webservern. Des Weiteren können sie kryptographische Algorithmen und das PKI-Konzept synthetisieren und beurteilen. Das Modul behandelt auch Themen wie Informationssicherheit, Kryptographie-Grundlagen, Verschlüsselungstechnologien, digitale Signaturen, X.509 Zertifikate und beinhaltet praktische Laborübungen.
         Die Studierenden erlangen im Kurs ein grundlegendes Wissen und Verständnis über Netzwerk- und Internet-Technologien sowie Basis-Dienste. Sie können Netzwerke und deren Basis-Dienste anwenden und verstehen. Darüber hinaus erhalten sie einen Überblick über aktuelle Netzwerk-Technologien und Basis-Dienste und können deren Funktionsweise beurteilen. Der Kurs behandelt Themen wie Schichtenmodelle, Übertragungsmedien, LANs, WLANs, Internet Protokolle, End-zu-end Kommunikation, Anwendungsdienste und Absicherung von Kanälen.
         Dieser Kurs führt Sie ein in die wichtigsten Aufgaben und Komponenten von Betriebssystemen, sowie die dafür verwendeten Algorithmen, Datenstrukturen und Hardware-Grundlagen. Sie lernen verschiedene Ansätze und Strategien kennen und analysieren und implementieren diese in praktischen Übungen. Weiter lernen Sie Schnittstellen und Techniken zum Implementierung von Systemprogrammen kennen, wenden diese an und üben, Probleme zu diagnostizieren und zu lösen.
         Dieses Modul führt die Studierenden in die Thematik der Algorithmen und Datentypen ein. Grundlegende algorithmische Techniken wie Rekursion, Divide and Conquer und Dynamic Programming werden vorgestellt und flache Datenstrukturen wie Stacks, Queues, Vektoren, Listen, Sequenzen, Sets und Dictionaries behandelt. Baumstrukturen, Graphen und entsprechende Algorithmen sind ebenfalls Teil der Diskussion.  Die Studierenden lernen in diesem Modul, wichtige Datenstrukturen und Algorithmen auf konkrete Problemstellungen anzuwenden und effizient zu implementieren.
         Am Ende dieses Moduls haben die Studierenden ein umfassendes Wissen und Verständnis über die Grundlagen der formalen Sprachen und Automatentheorie erworben. Sie sind in der Lage, deterministische und nicht-deterministische endliche Automaten zu entwickeln, reguläre Ausdrücke zu erstellen und Kellerautomaten für kontextfreie Sprachen zu entwerfen. Sie können schliesslich Parser implementieren und Compiler für kleine Sprachen erstellen.
         Nach Abschluss dieses Moduls haben die Studierenden ein fundiertes Wissen und Verständnis über die Theorie der Berechenbarkeit und Komplexität erlangt. Sie kennen den Unterschied zwischen erkennbaren und entscheidbaren Sprachen resp. Problemen und verstehen, dass einige Probleme unentscheidbar sind. Die Studierenden können die Entscheidbarkeit und NP-Vollständigkeit von Problemen nachweisen, indem sie Algorithmen oder Turing-Maschinen entwickeln und die Reduktionsmethode anwenden. Sie sind sich der Bedeutung der Komplexitätsklassen P und NP bewusst und verstehen die offenen Fragen, insbesondere die P- versus NP-Frage.
         unknown
         unknown
         **/

        String program = "BSc Informatik";
        double averageValue = TOTAL_VALUE / EXPECTED_MATCHES_COUNT;

        List<String> expectedKeywords = List.of(
                "programmiersprachen",
                "java",
                "kotlin",
                "programmierung",
                "objektorientierte anwendungen",
                "rechnerarchitekturen",
                "maschinensprachen",
                "c",
                "assembler",
                "netzwerke",
                "interaction design",
                "anforderungsmanagement",
                "software engineering",
                "softwareentwicklung",
                "uml",
                "it-controlling",
                "http",
                "rest-schnittstellen",
                "javascript",
                "kryptographie",
                "netzwerkprotokolle",
                "netzwerk- und internet-technologien",
                "netzwerk-technologien",
                "netzwerke",
                "internet-technologien",
                "software engineering-projekten",
                "betriebssysteme",
                "datenstrukturen",
                "algorithmen",
                "algorithmen und datenstrukturen",
                "datenbanken",
                "webtechnologien",
                "webanwendung",
                "informationssicherheit"
        );

        Optional<ProgramContent> programContent = programContentRepository.findByProgram(program);

        assertThat(programContent).isPresent();

        List<String> keys = programContent.get().getShortDescription().stream()
                .map(VisualDataEntry::getKey)
                .map(String::toLowerCase)
                .toList();

        assertThat(keys)
                .containsAnyElementsOf(expectedKeywords);

        double matches = keys.stream()
                .filter(expectedKeywords::contains)
                .count();

        assertThat(matches)
                .isGreaterThanOrEqualTo(EXPECTED_MATCHES_COUNT - TOLERANCE_KEYWORDS);

        programContent.get().getShortDescription().forEach(shortDescription -> {
            double value = shortDescription.getValue();
            assertThat(value)
                    .isBetween(averageValue - TOLERANCE_COMPETENCIES_VALUE, averageValue + TOLERANCE_COMPETENCIES_VALUE);
        });
    }
}
