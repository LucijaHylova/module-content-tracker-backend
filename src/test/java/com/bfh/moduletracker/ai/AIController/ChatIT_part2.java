package com.bfh.moduletracker.ai.AIController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.bfh.moduletracker.ai.config.PostgresTestcontainerConfiguration;
import com.bfh.moduletracker.ai.config.TestSecurityConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import({PostgresTestcontainerConfiguration.class,
        TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("inttest")
@Ignore
class ChatIT_part2 {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    ChatMemory chatMemory;


    @BeforeTestClass
    void setup() throws Exception {
        chatMemory.clear("default");
    }

    @Test
    void shouldReturnPositiveEnglishAnswerAboutJavaScriptModules() throws Exception {

        String query = "Are there any modules related to the JavaScript?";

        MvcResult result = mvc.perform(get("/ai/chat")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        String answer = json.get("answer").asText();

        assertThat(answer).isNotBlank();
        assertThat(answer.toLowerCase())
                .contains("web programming");
    }

    @Test
    void shouldReturnGermanAnswerWhenNoRelevantModulesFound() throws Exception {
        String query = "Ich interessiere mich für Module zur medizinischen Behandlung von Tieren.";

        MvcResult result = mvc.perform(get("/ai/chat")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        String answer = json.get("answer").asText();

        assertThat(answer).isNotBlank();
        assertThat(answer.toLowerCase())
                .containsAnyOf(
                        "keine",
                        "keine module",
                        "leider",
                        "keine module gefunden",
                        "nicht helfen",
                        "nicht weiterhelfen",
                        "nicht helfen kann",
                        "nicht weiterhelfen kann",
                        "no matching information is available",
                        "no matching information available",
                        "no",
                        "i'm sorry",
                        "sorry",
                        "i'm afraid",
                        "i can't");
    }

    @Test
    void shouldEnglishAnswerWhenNoRelevantModulesFound() throws Exception {

        String query = "I'm interested in modules about chemistry.";

        MvcResult result = mvc.perform(get("/ai/chat")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        String answer = json.get("answer").asText();

        assertThat(answer).isNotBlank();
        assertThat(answer.toLowerCase())
                .containsAnyOf("can't", "any modules", "any", "no", "no modules", "not found", "not help", "not further help", "not help can", "not further help can");
    }
}
