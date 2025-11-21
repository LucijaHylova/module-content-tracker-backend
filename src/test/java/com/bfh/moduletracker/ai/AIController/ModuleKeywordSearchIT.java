package com.bfh.moduletracker.ai.AIController;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.bfh.moduletracker.ai.common.FakeUserEntityGenerator;
import com.bfh.moduletracker.ai.config.PostgresTestcontainerConfiguration;
import com.bfh.moduletracker.ai.config.TestSecurityConfiguration;
import com.bfh.moduletracker.ai.controller.AuthController;
import com.bfh.moduletracker.ai.service.auth.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import({PostgresTestcontainerConfiguration.class, TestSecurityConfiguration.class})

@AutoConfigureMockMvc
@ActiveProfiles("inttest")
class ModuleKeywordSearchIT {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    private FakeUserEntityGenerator fakeUserEntityGenerator;

    @MockitoBean
    private AuthController authController;

    @Autowired
    private VectorStore vectorStore;

    @Test
    void shouldFindSicherheitRelatedModules() throws Exception {

        String query = "Sicherheit";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .containsAnyOf(
                        "security in communication systems",
                        "introduction to it-security",
                        "it-sicherheit und datenschutz in der wirtschaft"
                );
    }

    @Test
    void shouldFindSecurityRelatedModules() throws Exception {

        String query = "Security";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "security in communication systems",
                        "introduction to it-security",
                        "it-sicherheit und datenschutz in der wirtschaft"
                );
    }

    @Test
    void shouldFindProcessManagementModuleForBPMNWhenTwoKeywords() throws Exception {

        String query = "Geschäftsprocesse und BPMN";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "prozessmanagement und erp-systeme"
                );
    }

    @Test
    void shouldReturnNoModulesForGartenarbeitKeyword() throws Exception {
        String query = "Gartenarbeit";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonArray = new ObjectMapper().readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            namesDe.add(module.path("name").path("de").asText().toLowerCase());
        }

        assertThat(namesDe).isEmpty();
    }

    @Test
    void shouldFindModulesWhenDepartmentFilter() throws Exception {

        String query = "Sicherheit";
        String department = "Wirtschaft und Management";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query)
                        .param("department", department))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "it-sicherheit und datenschutz in der wirtschaft"
                )
                .doesNotContain(
                        "security in communication systems",
                        "introduction to it-security"
                );
    }

    @Test
    void shouldFindModulesWhenProgramFilter() throws Exception {

        String query = "Geschäftsmodelle";
        String program = "Wirtschaftsinformatik";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query)
                        .param("program", program))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "e-commerce und digitale märkte",
                        "digitale geschäftsprozesse und automatisierung",
                        "mobile business und app-entwicklung"
                )
                .doesNotContain(
                        "geschäftsmodelle der digitalen wirtschaft",
                        "einführung in digital Business"
                );
    }

    @Test
    void shouldFindModulesWhenProgramFileIncludesAllElectiveModules() throws Exception {

        String query = "databases";
        String program = "Wirtschaftsinformatik";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query)
                        .param("program", program))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "cloud computing und datenmanagement"
                )
                .doesNotContain(
                        "databases"
                );
    }

    @Test
    void shouldFindModulesWhenDepartmentAndProgramFilter() throws Exception {

        String query = "Geschäftsmodel";
        String department = "Wirtschaft und Management";
        String program = "Digital Business";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query)
                        .param("department", department)
                        .param("program", program))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "geschäftsmodelle der digitalen wirtschaft",
                        "einführung in digital business"

                )
                .doesNotContain(
                        "e-commerce und digitale märkte",
                        "digitale geschäftsprozesse und automatisierung",
                        "cloud computing und datenmanagement",
                        "mobile business und app-entwicklung",
                        "prozessmanagement und erp-systeme"
                );
    }

    @Test
    void shouldFindModulesWhenTwoKeywords() throws Exception {

        String query = "warehouse data";

        MvcResult result = mvc.perform(get("/ai/findByKeywords")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonArray = mapper.readTree(responseBody);

        List<String> namesDe = new ArrayList<>();
        for (JsonNode module : jsonArray) {
            String nameDe = module.path("name").path("de").asText();
            namesDe.add(nameDe.toLowerCase());
        }

        assertThat(namesDe)
                .contains(
                        "business intelligence und data warehousing"
                );
    }
}
