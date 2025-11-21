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
import com.bfh.moduletracker.ai.model.entity.ProgramContent;
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
class ProgramContentCompetenciesIT {

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

        mvc.perform(get("/ai/getProgramContentCompetenciesData"))
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
    void shouldValidateCompetenciesContentCategoryForInformatikProgram() {
        /**
         Original Module Competencies of Informatik Program:
         unknown
         unknown
         unknown
         unknown

         Wissen, Verstehen: Am Ende dieses Moduls müssen die Studierenden in der Lage sein, die Standardsensoren für nichtelektrische Grössen aufzuzählen und deren Messprinzipien und Eigenschaften beschreiben zu können.
         Anwenden von Wissen und Verstehen: Am Ende dieses Moduls sind die Studierenden in der Lage, ein Sensorsystem mit nichtelektrischen Messgrössen vom Wandlerelement bis zum Nutzsignal zu definieren und Lösungsvarianten aufzuzeigen. Die Studierenden können Störeinflüsse orten und die Messresultate bezüglich der Messfehler beurteilen.
         Urteilen: Ausgehend von detaillierten technischen Spezifikationen sind die Studierenden am Ende dieses Moduls in der Lage, Lösungsvarianten eines Sensorsystems mit nichtelektrischen Messgrössen zu evaluieren und zu beurteilen.
         Wissen, Verstehen:  Am Ende dieses Moduls kennen die Studierenden die wichtigsten Konzepte aus der Linux-Welt mit Fokuss Embedded Systeme.  Anwenden von Wissen und Verstehen:  Am Ende dieses Moduls sollten die Studierenden in der Lage sein:
         Ein Linux System zu Bedienen und auf der Kommandozeile Applikationen zu bedienen.
         Ein Embedded Linux System auf einem Embedded Development Board  in Betrieb zu nehmen und dieses zu bedienen und zu erweitern.
         Einfache Applikationen zu implementieren.
         Einfache Gerätetreiber zu programmieren und zu testen
         Analyse/Organisieren/Problemlösen/Reflektieren:  Anhand eines Gruppenprojekts lernen die Studierenden den Transfer der Theorie in die Praxis. Sie werden selbständig projektspezifisches Wissen erarbeiten und einsetzen. Am Schluss der Veranstaltung präsentiert jede Gruppe das individuelle Projekt und gibt dem Publikum anhand einer Reflexion einen Einblick in die Ergebnisse und Potentiale des Mini-Projekts.
         Am Ende dieses Moduls sollten die Studierenden in der Lage sein:   Wissen und Verstehen:
         Methoden zur      Positionsbestimmung wie zum Beispiel GPS verstehen und beschreiben können.
         Die Eigenschaften (Vorteile und Nachteile) von verschiedenen Navigationssystemen zu kennen
         Anwenden von Wissen und Verstehen:
         Im Rahmen eines Mini-Projektes eine eigene Aufgabe bearbeiten (zbsp. mit dem GNSS/GPS Evaluationskit)
         Urteilen:
         Für eine konkrete Anwendung      verschiedene Positionsbestimmungs- und Navigationssyteme zu bewerten und      die geeignete Lösung auszuwählen.
          



         Wissen und Verstehen : Die Studierenden
         wenden die Grundbegriffe der Informationssicherheit korrekt an
         kennen die weitverbreiteten Schwachstellen der ICT und wissen welche Gegenmassnahmen angebracht sind.
         erkennen die potentiellen Gefahrenstellen beim Auf-, Aus- oder Umbau der ICT Infrastruktur

         Anwenden von Wissen und Verstehen : Die Studierenden
         wenden Unterstützungswerkzeuge sinnvoll an
         können ein Netzwerk selbständig auf Schwachstellen überprüfen
         ergreifen selbständig die zur Sicherung notwendigen Massnahmen
         helfen bei der Entwicklung von Elementen zu den Sicherheitsprozessen im Unternehmen

         Urteilen : Die Studierenden
         beurteilen die wesentlichen Elemente der ICT Sicherheit innerhalb einer Unternehmung
         beurteilen die unterschiedlichen technischen Sicherheitsaspekte der  ICT-Infrastruktur sowie von Informationen, Applikationen und Kommunikationsprotokollen.

         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen die Sprachkonstrukte von Java und verstehen die objektorientierten Prinzipien.   2. Anwenden und Analyse   Die Studierenden können Anwendungsprobleme selbstständig analysieren, modellieren (u.a. mittels UML-Klassendiagrammen) und in eine objektorientierte Lösung überführen.  Sie beherrschen die Ausführung von Konsolenapplikationen, können diese zur Laufzeit überwachen (u.a. mittels Logfiles) und Fehler beheben (debuggen).  Sie sind in der Lage, Ausnahmen (Exceptions) effektiv zu behandeln.  Sie sind fähig, Unit Tests zu schreiben und für das Testen von eigener Klassen und Applikationen einzusetzen.   Sie sind in der Lage, einfache Desktop-Applikationen mit JavaFX zu entwickeln und auszuführen.   3. Synthese und Beurteilen   Die Studierenden können auf der Basis von existierenden Frameworks und eigenem Code vollständige Applikationen entwickeln.  Sie sind in der Lage, die Verwendung von Programmkonstrukten kritisch zu vergleichen.  Sie können die Leistungsfähigkeit und mögliche Risiken vor der Ausführung einer Java-Applikation abschätzen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen die wichtigsten Sprachkonstrukte (Variablen, Kontrollstrukturen wie Schleifen und Auswahl) und die grundlegenden Datentypen von Kotlin (und können diese ihren Entsprechungen in Java gegenüberstellen).  Sie haben ein Verständnis der Konzepte Unveränderlichkeit (Immutability) und Null-Safety.  Sie  kennen die Bausteine der Objektorientierung (Klassen, Objekte, Properties, Methoden, Interfaces) und verstehen die dahinterstehenden Prinzipien (Abstraktion, Vererbung, Polymorphismus, Enkapsulation).  Sie kennen die Konzepte der funktionalen Programmierung (Functions, Operations, Lambdas).  Sie können komplexe Datentypen (Arrays, Collections, etc.) einordnen und verwenden.   2. Anwenden von Wissen und Verstehen   Die Studierenden sind imstande, die behandelten Sprachkonstrukte von Kotlin (und Java) in kleineren und mittelgrossen Programmen anzuwenden, zu testen (Unit-Testing) und mittels UML/Kdoc zu dokumentieren.  Sie sind in der Lage, Klassen zu identifizieren und sinnvoll zueinander in Beziehung zu setzen.  Sie entwickeln objektorientierte Programme, in denen Objekte miteinander interagieren, um ein Anwendungsproblem zu lösen.  Sie können entscheiden, wann funktionale Aspekte sinnvoll eingesetzt werden.  Sie können schliesslich bestimmen, wann Kotlin-Programme durch Java-Objekte und -Bibliotheken erweitert werden sollten.   3. Synthese und Beurteilen   Die Studierenden verstehen und analysieren Anwendungsprobleme und können diese in eine korrekte Lösung überführen.  Sie sind fähig, die Korrektheit der Lösung mit Unittests zu demonstrieren.  Sie erkennen die Vor- und Nachteile von objektorientierten und funktionalen Programmteilen und können ggf. die Qualität der Implementierung verbessern.  Sie können Lösungen in Kotlin und Java vergleichen, bewerten und ineinander überführen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden verstehen die Funktionsweise von Generics, Streams und Threads.  Sie kennen die Collections-, I/O- und JavaFX-Bibliotheken.  Sie wissen um die konkreten Vorteile, aber auch die Risiken der Rekursion.  Sie kennen das Java Modulsystem und seine Möglichkeiten, Abhängigkeiten im Code zu definieren und zu kontrollieren.   2. Anwenden und Analyse   Die Studierenden sind in der Lage, mittelgrosse objektorientierte Anwendungen zu entwerfen und mit passenden Java-Technologien wie Generics, Collections, Lambdas oder Rekursion umzusetzen.  Sie sind kompetent im Umgang mit UML-Klassendiagrammen, um Ihren Code zu strukturieren.  Sie sind fähig, mit Java-Applikationen Daten- und Text- Dateien zu bearbeiten, insbesondere mit den Formaten XML und JSON (sowie der dabei nötigen Serialisierung) umzugehen.  Sie können eigene Ausnahmen erzeugen und behandeln.  Sie verstehen es, mehrsprachige GUI-Applikationen zu entwerfen und erweitern.   3. Synthese und Beurteilen   Die Studierenden können komplexe Anwendungsprobleme eigenständig analysieren, geeignete Bibliotheken evaluieren und integrieren.  Sie sind in der Lage, eine technisch fundierte Lösung zu entwickeln, indem sie Bibliotheksfunktionen mit eigenentwickeltem Code kombinieren.  Sie sind fähig, Architekturvarianten zu vergleichen und bewerten.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden verstehen die verschiedenen Methoden des Dateimanagements und der Objekt-Persistenz (Serialisierung, XML, JSON) sowie dazu notwendige Konzepte, wie Annotations und Reflection.   Sie kennen die Methoden zum Umgang und Behandeln von Fehlern und können mit Exceptions sinnvoll umgehen.  Sie verstehen den Aufbau von graphischen User Interfaces basierend auf einem Model-View-Controller Prinzip und kennen die Konzepte zur Internationalisierung von UIs.  Sie kennen die Prinzipien der Rekursion und deren Beschränkungen, von generischen Datentypen, inkl. Subtyping und Covarianten, und vom Überladen (Overloading) von Methoden und Operatoren.  Sie haben das Prinzip der asynchronen Programmierung verstanden und wissen, wie dieses in Kotlin (Kotlin coroutines vrs. Java threads) realisiert wird.   2. Anwenden und Analyse   Die Studierenden können eine sinnvolle Fehlerbehandlung implementieren und mit Exceptions umgehen (Werfen, Behandeln, Definieren).  Sie können einfache graphische User Interfaces implementieren und internationalisieren.  Sie sind in der Lage, rekursive Algorithmen zu erkennen und implementieren.  Sie können generische Datentypen sicher verwenden, eigene entwickeln und implementieren.   3. Synthese und Beurteilen   Die Studierenden verstehen und analysieren komplexe Anwendungsprobleme und können diese in eine korrekte Lösung überführen.  Sie können selbstständig geeignete Konzepte und Prinzipien (functional vrs. object-oriented) auswählen, um eine optimale Lösung zu erreichen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden verstehen die Grundlagen von Rechnerarchitekturen und die Prinzipien von Maschinensprachen.  Sie wissen, was ein Compiler ist und kennen die Sprachkonstrukte der Programmiersprache C.  Sie kennen die wichtigsten Entwicklungswerkzeuge für C- und Assembler-Projekte.   2. Anwenden und Analyse   Die Studierenden verstehen, wie Zahlen und Zeichenketten in Computern dargestellt werden und können zwischen den verschiedenen Darstellungsformen konvertieren.  Sie können einfache Programme in Maschinensprache lesen und verstehen.  Sie können selbstständig einfache Programme in C entwickeln.   3. Synthese und Beurteilen   Die Studierenden verstehen die Bedeutung und die wichtigsten Ursachen für undefiniertes Verhalten von Software.  Sie sind in der Lage, einfache Programme zu debuggen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen die zentrale Bedeutung von Interaction Design / Human Centered Design für das Entwickeln von erfolgreich einsetzbaren Software Produkten und verstehen die grundlegenden Ansätze und Methoden, die dabei zum Einsatz kommen.  Sie verstehen auch warum es wichtig ist, dass das Design von technischen Artefakten, die von und mit Menschen eingesetzt werden sollen, als sozio-technisches Design zu verstehen ist, welches neben den eher technischen Aspekten auch zutiefst menschliche und organisatorische Fragestellungen berücksichtigen muss.   2.  Anwenden und Analyse   Die Studierenden können die Ansätze und Methoden des Interaction Design / Human Centered Design auf gegebene Problemstellungen anwenden.  Sie können ihre Kenntnisse anhand von eigenen Analysen und Lösungsvorschlägen fachgerecht artikulieren bzw. demonstrieren.   3. Synthese und Beurteilen   Die Studierenden können nicht nur gegebene Problemstellungen analysieren, sondern auch bereits installierte Software Produkte auf ihre Qualitäten hinsichtlich des Interaction Design / Human Centered Design / Usability analysieren und ggf. Verbesserungsvorschläge machen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden können die Definitionen und Begriffe des Anforderungsmanagements (Requirements Engineering) erläutern.  Sie sind in der Lage, die erfolgskritischen Rahmenbedingungen des Anforderungsmanagements für ein Projekt zu benennen.  Sie können die Vor- und Nachteile der behandelten Ansätze zum Anforderungsmanagement darstellen.   Sie verstehen die Rolle des Anforderungsmanagements (Requirements Engineering) im Rahmen des Projektmanagements (unter Einbezug erster ausgewählter elementarer Aspekte des Projektmanagements).   2.  Anwenden und Analyse   Die Studierenden sind imstande, (Projekt-)Ziele und Vorgaben korrekt zu erfassen und zu operationalisieren.  Sie sind fähig, ein problemadäquates und sachgerechtes Anforderungsmanagement (auch bei agiler Vorgehensweise) zu etablieren und damit beizutragen, die Projektführungsmethode auf den System- und Software-Entwicklungsprozess abzustimmen.  Sie sind in der Lage, korrekte Anforderungsdokumentationen zu verfassen, die als Basis für weiterführende Artefakte dienen können und zur (Projekt-)Zielerreichung - d.h. letztlich zum Projekterfolg - beitragen   3. Synthese und Beurteilen   Die Studierenden können Ausgangslage beurteilen, die Rolle des Requirements Engineers darauf abstimmen und mögliche Konfliktsituationen und Problemfelder im Projektablauf sowohl identifizieren als auch als Berater und Moderator einer Lösung zuführen.  Sie sind in der Lage, die (allenfalls frei geäusserten) Wünsche der Auftraggeber, Stakeholder und Akteure ("was" das gewünschte System leisten soll) korrekt aufnehmen und in zielorientierte strukturierte Darstellungen überführen, die als abgestimmte und überprüfte Basis für den weiteren Projektablauf in Folgeartefakten eingesetzt werden können.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen den grundsätzlichen Ablauf von Software Engineering-Projekten mit den Kernelementen Requirements Engineering, Design und Implementierung, Qualitätssicherung, Auslieferung und Betrieb.  Sie kennen den Unterschied zwischen agiler und plan-gesteuerter Vorgehensweise.  Die Studierenden haben einen Überblick über gängige Tools zur Projektabwicklung.  Nach Abschluss des Moduls können die Studierenden die Struktursicht, die Verhaltenssicht und die Verteilungssicht von Software-Systemen mit UML modellieren.  Sie kennen die SOLID- sowie weitere Entwurfsprinzipien. Sie kennen die wichtigsten Entwurfsmuster.   2. Anwenden und Analyse   Die Studierenden können exemplarische Software Engineering-Prozesse entwerfen und können diese mit geeigneten Software-Tools im Team umsetzen.  Die Studierenden sind in der Lage, die Architektur und den Entwurf eines Software-Systems zu dokumentieren.  Sie sind in der Lage, die behandelten Entwurfsmuster sinnvoll einzusetzen.   3. Synthese und Beurteilen   Die Studierenden sollen am Ende des Moduls die Kompetenz haben, die richtigen Entwurfsentscheide zu fällen.  Im Weiteren sollen sie die Kompetenz haben, die zur Erreichung der geforderten Qualität einer Software geeigneten Massnahmen zu ergreifen.  Sie sind fähig, gut strukturierte, flexible und erweiterbare Software-Systeme zu konzipieren, zu implementieren, zu testen und zu dokumentieren, welche die Anforderungen in Bezug auf Funktionen und Qualität erfüllen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden sind in der Lage, die wesentlichen Einflussgrössen des Umfelds auf ein Unternehmen zu beschreiben.  Sie können die Zielsetzungen und Aktivitäten des Finanz- und IT-Controlling aufzuzeigen und wichtige betriebliche Problemstellungen und entsprechende Lösungsmethoden benennen.  Sie verstehen es dabei insbesondere, die spezifischen Problemstellungen rund um den Einsatz von IT-Mitteln in Unternehmen zu überblicken.   2. Anwenden und Analyse   Die Studierenden beherrschen die Handhabung durchgängiger Planungsprozesse vom Absatz bis zur Beschaffungsfunktion.  Sie können Kriterien für die Beschaffung von IT-Leistungen im Cloud-Zeitalter anwenden.  Sie sind in der Lage, eine einfache Buchhaltung zu führen, die den gesetzlichen Erfordernissen genügt.  Sie sind ebenfalls fähig, eine einfache Kostenrechnung zu führen, inkl. Ermittlung des kurzfristigen Betriebserfolges, der Kalkulation der Selbstkosten von Produkten oder Leistungen, oder der Kalkulation von Verrechnungspreisen für die Leistungsverrechnung.   3. Synthese und Beurteilen   Die Studierenden sind fähig, Eigenschaften von unternehmensunterstützenden Softwareapplikationen zu analysieren und bewerten.  Sie können die finanzielle Funktionsweise und Situation von IT-Unternehmen und IT-einsetzenden Unternehmen ermitteln.  Sie beherrschen die Anwendung und Interpretation wichtiger Instrumente des Rechnungswesens (z. B. Geldflussrechnung).  Sie sind in der Lage, Prozesse rund um die Beschaffung und den Einsatz von IT-Mitteln im Unternehmen kritisch zu beurteilen und wo nötig zu verbessern.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden sind in der Lage, die Voraussetzungen für das Zustandekommen eines gültigen Vertrages zu benennen und eine entsprechende grobe Beurteilung allfälliger Risiken bei der Vertragsentstehung und -abwicklung vornehmen zu können  Sie können die Voraussetzungen für den rechtlichen Schutz von Computerprogrammen und die Zuordnung der Rechte zu den involvierten Parteien benennen.   2. Anwenden und Analyse   Die Studierenden sind in der Lage, einfache Probleme im Zusammenhang mit der Auslegung von Software-spezifischen Projekt-, Lizenz- und Wartungsverträgen (Kaufvertrag, Werkvertrag, Auftrag) selbständig zu analysieren.   3. Synthese und Beurteilen   Die Studierenden sind in der Lage, auftretende rechtliche Fragen im Internet (privat- und strafrechtlich) sowie allfällige aus der geschäftlichen Aktivität entstehende Problemstellungen zu erkennen und eine Grobbeurteilung vornehmen zu können.
         1. Wissen und Verstehen  - Sie kennen die Funktionsweise von HTTP und verstehen den Aufbau einer Webanwendung. - Sie verstehen die Prinzipien einer REST-Schnitstelle. - Sie kennen JavaScript und wissen, wie es im Browser verwendet wird.   2. Anwenden und Analyse  - Sie können eine REST-Schnitstelle entwerfen und implementieren. - Sie sind imstande, mit JavaScript eine Single-Page-Applikation zu implementieren.   3. Urteilen und Probleme bearbeiten  - Sie können die verschiedenen Aspekte einer Webanwendung analysieren. - Sie können unterschiedliche Architekturen von Webanwendungen beurteilen.
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

         können  für gegebene Problemstellungen im Bereich des Entwurfs, der   Implementierung sowie der Anwendung relationaler Datenbanken   verschiedene Lösungsmöglichkeiten beurteilen sowie eigene   Lösungskonzepte erstellen und implementieren.



         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden verstehen die Grundlagen der symmetrischen und asymmetrischen Kryptographie, die Konzepte einer Public Key Infrastruktur (PKI) und kennen gängige Authentisierungsverfahren.  Sie können die Zusammenhänge zwischen digitalem Signaturgesetz (ZertES) und dem Einsatz von qualifizierten Zertifikaten in der Schweiz aufzeigen.   2. Anwenden und Analyse   Die Studierenden kennen Aufbau und Handling einer einfachen X.509 Certification Authority (CA).  Sie können den Umgang und die Konfiguration von Secure Messaging Applikationen (S/MIME) in der Praxis umsetzen.  Sie können Aufbau und Konfiguration eines SSL/TLS-unterstützenden Webservers realisieren.   3. Synthese und Beurteilen   Die Studierenden können den Einsatz von kryptographischen Algorithmen in heutigen Anwendungen richtig evaluieren und einplanen.  Sie verstehen das Konzept einer PKI als Basis verschiedenster Dienste im Zusammenhang mit Sicherheitslösungen und können dieses miteinbeziehen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen Grundbegriffe, Schichtenmodelle und Standardisierungs- und Normierungs-Gremien im Netzwerkbereich.  Sie kennen die aktuellen Link-, Netzwerk-, Transport-, und Anwendungs-Technologien, -Protokolle und -Schnittstellen sowie die wichtigsten Internet Basis-Dienste und Anwendungs-Protokolle.   2. Anwenden und Analyse   Die Studierenden sind in der Lage, Internet Basis-Dienste korrekt zu nutzen und bei Bedarf zu konfigurieren.  Die Studierenden können Netzwerkprobleme analysieren und Lösungsansätze identifizieren   3. Synthese und Beurteilen   Die Studierenden sind in der Lage die Vor- und Nachteile verschiedener Netzwerk-Technologien und -Protokolle zu beurteilen.  Sie sind in der Lage beim Entwurf von Netzwerk-Umgebungen und -Diensten mitzuwirken, unter Berücksichtigung von Anforderungen an Leistung, Sicherheit und Skalierbarkeit.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Sie kennen die wichtigsten Aufgaben eines Betriebssystems  Sie verstehen die dafür eingesetzten Algorithmen, Datenstrukturen und Hardware-Grundlagen   2. Anwenden und Analyse   Sie können Problemstellungen analysieren, beurteilen und geeignete Lösungsansätze identifizieren  Sie sind in der Lage, diese in einem Systemprogramm zu implementieren und auf seine Korrektheit hin zu testen   3. Synthese und Beurteilen   Sie verfügen über ein vertieftes Verständnis bei der praktischen Arbeit mit Betriebssystemen (z. B. als Administrator, im Cloud-Kontext, etc.) und der Diagnose und Behebung von Problemen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden können grundlegende Datenstrukturen und Algorithmen beschreiben und deren Unterschiede aufzeigen.  Sie kennen die Komplexitäten der Hauptoperationen.  Sie können entsprechende Anwendungsgebiete benennen.   2. Anwenden und Analyse   Die Studierenden können grundlegende Datenstrukturen und Algorithmen auf konkrete Problemstellungen anwenden und effizient implementieren.  Sie sind in der Lage, Datenstrukturen und Algorithmen auf konkrete Problemstellungen hin anzupassen.   3. Synthese und Beurteilen   Die Studierenden können für konkrete Problemstellungen geeignete Datenstrukturen bzw. entsprechende Algorithmen auswählen.  Sie können diese Auswahl begründen, d.h. insbesondere deren jeweiliges Verhalten analysieren und mit Alternativen vergleichen.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen die formalen Definitionen von Alphabet, Wort, Sprache und damit verbundene Operationen.  SIe kennen die Definition des endlichen Automaten (deterministisch und nicht-deterministisch), des regulären Ausdrucks und der regulären Grammatik.  Sie verstehen, dass diese drei Formalismen gleichwertig sind und die Klasse der regulären Sprachen definieren.  Sie haben erkannt, dass endliche Zustandsautomaten Grenzen haben und dass eine leistungsfähigere Maschine, nämlich der Kellerautomat (Pushdown-Automat), entworfen werden muss, um diese Grenzen zu überwinden.  Sie begreifen, dass Kellerautomaten die Klasse der kontextfreien Sprachen definieren und verstehen die Bedeutung dieser Formalismen für die lexikalische und syntaktische Analyse und Kompilierung.  Schliesslich verstehen die Studierenden die Notwendigkeit, einen dritten Maschinentyp, die sogenannte Turing-Maschine, zu definieren, der die Einschränkungen von Kellerautomaten überwindet. Sie begreifen, dass jeder Automatentyp eine Klasse von Sprachen definiert. Sie kennen auch die Beziehungen zwischen diesen Sprachklassen, zusammengefasst in der Chomsky-Sprachen-Hierarchie.   2. Anwenden und Analyse   Die Studierenden sind in der Lage, deterministische und nicht-deterministische endliche Zustandsautomaten zu entwickeln.  Sie können nicht-deterministische Automaten in deterministische Automaten transformieren und verstehen die Schwierigkeit der mit einer solchen Transformation verbundenen kombinatorischen Explosion in der Praxis.  Die Studierenden sind fähig, deterministische endliche Zustandsautomaten zu minimieren, um die für ihre Implementierung benötigten Ressourcen zu reduzieren.  Sie sind auch in der Lage, reguläre Ausdrücke entwickeln und sie in endliche Zustandsautomaten transformieren und umgekehrt.  Sie können Kellerautomaten für einige kontextfreie Sprachen entwickeln und verstehen die Notwendigkeit solcher Automaten für die Analysetätigkeit.   3. Synthese und Beurteilen   Am Ende des Moduls entwickeln die Studierenden einen Parser unter Verwendung eines "Parser Generators" (ein Werkzeug, das im Modul vorgestellt wird) und integrieren die Codegenerierung, um einen Übersetzer oder Compiler für eine kleine Sprache zu implementieren.   Die Studierenden erkennen, dass eine solche Implementierung fast alle im Modul vorgestellten Begriffe beinhaltet.
         Nach Abschluss des Moduls haben die Studierenden die folgenden Kompetenzen auf den angegebenen Niveaustufen erworben:   1. Wissen und Verstehen   Die Studierenden kennen den Unterschied zwischen erkennbaren und entscheidbaren Sprachen oder Problemen.  Sie verstehen, dass einige Probleme unentscheidbar sind und dass es keine Algorithmen für diese Art von Problemen gibt.  Die Studierenden haben gesehen, dass entscheidbare Probleme nach ihrer Zeitkomplexitätsklasse klassifiziert werden.  Sie wissen, dass die Anwendung "roher Gewalt" oft zu exponentiellem Verhalten führt und dass ihre Anwendung deshalb unrealistisch ist.  Sie verstehen auch, dass die Komplexitätsklasse P-Problemen entspricht, deren Implementierung im Gegensatz zur Klasse NP effektiv ist.  Sie begreifen, dass die Unterklasse NPC (NP-vollständig) der NP die schwierigsten Probleme von NP enthält.  Sie sind sich bewusst, dass viele Probleme, die in der Praxis wichtig sind, zu dieser Unterklasse der NPC gehören und dass es keine bekannten wirksamen Lösungen für diese Probleme gibt.   2. Anwenden und Analyse   Die Studierenden sind in der Lage, die Entscheidbarkeit bestimmter Probleme durch die Entwicklung von Algorithmen oder Turing-Maschinen nachzuweisen.  Zum Nachweis der Unentscheidbarkeit können sie die Reduktionstechnik anwenden.  Es werden mehrere NP-komplette Probleme untersucht, und die Studierenden sind in der Lage, die NP-Komplettheit vieler Probleme mit Hilfe der Polynomreduktionstechnik nachzuweisen.   3. Synthese und Beurteilen   Die Studierenden verstehen, wie wichtig der Begriff der NP-Vollständigkeit ist und dass viele Fragen in der wissenschaftlichen Gemeinschaft noch offen sind, insbesondere die Beziehung zwischen den beiden Komplexitätsklassen P und NP (P gleich NP oder P ungleich NP).  Konfrontiert mit einem gegebenen Problem können die Studierenden die entsprechende Problemklasse auswählen und eine Reduktion entwickeln, um die Unentscheidbarkeit oder NP-Vollständigkeit nachzuweisen.
         Am Ende dieses Moduls haben die Studierenden folgende Fähigkeiten erworben:    1. Wissen und Verstehen   Die Studierenden kennen:
         Stromkreise
         Digitalisierung von Analogsignalen
         Kinematik physikalischer Grössen
         Graphische Darstellung von Daten in ThingSpeak
         2. Anwenden von Wissen und Verstehen   Die Studierenden verstehen und können anwenden:
         Ansteuerung von LED mit dem Microprozessor
         Analogsignale bei MulitColor-LED
         AD-Wandler
         Der Einsatz des TRMP36 und Software-Kalibrierung
         Optische Abstandsmessungen
         Netzwerkscanner, WLAN-Thermometer,
         Daten ins Internet senden, der Microprozessor als eigener Webserver
         Raumüberwachung mit Lichtsensoren
         3. Urteilen und Probleme bearbeiten   Die Studierenden können:
         Einschätzung, welche Sensoren für welche Probleme eingesetzt werden
         Verstehen der Zeitentwicklung gemessener Daten und Beschreibung mit korrekten physikalischen Begriffen
         Entwicklung eines eigenen IoT-Projektes (von der Idee bis zur Umsetzung)

         Am Ende dieses Moduls haben die Studierenden folgende Fähigkeiten erworben:  1. Wissen und Verstehen  Die Studierenden kennen die physikalischen Grundlagen der Optik und Mechanik.  2. Anwenden von Wissen und Verstehen  Das Modul ermöglicht den Studierenden die physikalischen Grundlagen auf CPVR anzuwenden und zu verstehen.  3. Urteilen und Probleme bearbeiten  Die Studierenden können physikalische Vorgänge mit Modellen beschreiben und visualisieren.
         **/

        String department = "BSc Informatik";
        double averageValue = TOTAL_VALUE / EXPECTED_MATCHES_COUNT;

        List<String> expectedKeywords = List.of(
                "daten analysieren",
                "java programmieren",
                "java anwendungen entwickeln",
                "java programme schreiben",
                "java programme testen",
                "java programme debuggen",
                "java programme anwenden",
                "java programme entwickeln",
                "kotlin programme anwenden",
                "kotlin programmieren",
                "javafx entwickeln",
                "javafx ausführen",
                "javafx ausführen und entwickeln",
                "probleme lösen",
                "tests schreiben",
                "code debuggen",
                "schnittstellen entwerfen",
                "webanwendungen entwickeln",
                "netzwerke konfigurieren",
                "applikationen implementieren",
                "ausnahmen behandeln",
                "systeme modellieren",
                "software entwickeln",
                "software testen",
                "architektur dokumentieren",
                "linux administrieren",
                "linux system administrieren",
                "linux system bedienen",
                "embedded linux system betreiben",
                "fehler erkennen",
                "lösungen bewerten",
                "led mit dem microprozessor anwenden",
                "led mit dem microprozessor wissen",
                "anforderungen definieren",
                "datenbanken abfragen",
                "datenbanken anbinden",
                "datenbanken implementieren",
                "datenbanken anwenden",
                "datenbanken implementieren und anwenden",
                "daten strukturieren",
                "datenbanken entwerfen",
                "datenbanken modellieren",
                "navigationssysteme kennen",
                "navigationssysteme bewerten",
                "algorithmen anwenden",
                "algorithmen entwickeln",
                "algorithmen implementieren",
                "prozesse planen",
                "technologien vergleichen",
                "projekte umsetzen",
                "funktionen kombinieren",
                "lösungsansätze evaluieren",
                "programme testen",
                "messfehler beurteilen",
                "rekursive algorithmen entwickeln",
                "verträge beurteilen",
                "benutzerschnittstellen gestalten",
                "designprinzipien anwenden",
                "sicherheitsmassnahmen umsetzen",
                "daten sichern",
                "urteilen und probleme bearbeiten",
                "messsysteme beurteilen"
        );


        Optional<ProgramContent> programContent = programContentRepository.findByProgram(department);

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
