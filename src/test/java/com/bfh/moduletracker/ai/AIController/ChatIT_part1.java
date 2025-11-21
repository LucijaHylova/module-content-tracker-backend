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
class ChatIT_part1 {

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
    void shouldReturnPositiveGermanAnswerAboutSecurityModules() throws Exception {

        String query = "Gibt es Module, die sich mit Sicherheitsthemen beschäftigen?";

        MvcResult result = mvc.perform(get("/ai/chat")
                        .param("query", query, "sessionId", "default"))

                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        String answer = json.get("answer").asText();

        assertThat(answer).isNotBlank();
        assertThat(answer.toLowerCase())
                .containsAnyOf("security in communication systems", "introduction to it-security", "it-sicherheit und datenschutz in der wirtschaft");

        String queryCon = "Welche von diesen Modulen sind Pflichtmodule?";

        MvcResult resultCon = mvc.perform(get("/ai/chat")
                        .param("query", queryCon, "sessionId", "default"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBodyCon = resultCon.getResponse().getContentAsString();
        ObjectMapper mapperCon = new ObjectMapper();
        JsonNode jsonCon = mapperCon.readTree(responseBodyCon);
        String answerCon = jsonCon.get("answer").asText();

        assertThat(answerCon).isNotBlank();
        assertThat(answerCon.toLowerCase())
                .containsAnyOf("security in communication systems", "introduction to it-security");

    }

    @Test
    void shouldReturnPositiveGermanAnswerAboutDataModules() throws Exception {

        String query = "Ich interessiere mich für Module, die sich mit der Arbeit an Daten beschäftigen - wie deren Verarbeitung, Verwaltung oder Speicherung.";

        MvcResult firstResult = mvc.perform(get("/ai/chat")
                        .param("query", query))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = firstResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        String answer = json.get("answer").asText();

        assertThat(answer).isNotBlank();
        assertThat(answer.toLowerCase())
                .containsAnyOf("business intelligence und data warehousing", "databases", "datenanalyse für wirtschaftsinformatiker");
    }
}
