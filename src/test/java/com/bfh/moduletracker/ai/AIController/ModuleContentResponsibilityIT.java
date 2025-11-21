package com.bfh.moduletracker.ai.AIController;

import java.util.ArrayList;
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
class ModuleContentResponsibilityIT {

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
    void shouldValidateResponsibilityContentCategoryForModuleBTI1002() {
        validateResponsibility("BTI1002", List.of("A. Laube"));
    }

    @Test
    @Transactional
    void shouldValidateResponsibilityContentCategoryForModuleBTI1331() {
        validateResponsibility("BTI1331", List.of("H. Wenger"));
    }

    @Test
    @Transactional
    void shouldValidateResponsibilityContentCategoryForModuleBSS3204() {
        validateResponsibility("BSS3204", List.of("C. Fraefel", "C. Michel"));
    }

    @Test
    @Transactional
    void shouldValidateResponsibilityContentCategoryForModuleBSS1203_50() {
        validateResponsibility("BSS1203.50", List.of(
                "E. Ammann Dula",
                "D. Kessler",
                "C. Schenker"
        ));
    }

    @Test
    @Transactional
    void shouldValidateResponsibilityContentCategoryForModuleWI104() {
        validateResponsibility("WI104", List.of("unknown", "", " ", "keine", "keine Angabe"));
    }

    private void validateResponsibility(String code, List<String> expected) {
        Optional<ModuleContent> moduleContent = moduleContentRepository.findById(code);

        assertThat(moduleContent)
                .isPresent();

        List<String> actualResponsibility = new ArrayList<>(moduleContent.get().getResponsibility());

        assertThat(actualResponsibility)
                .isNotNull();

        boolean validRequirements = actualResponsibility.isEmpty() || expected.stream().anyMatch(actualResponsibility::contains);

        assertThat(validRequirements)
                .isTrue();
    }
}
