package com.bfh.moduletracker.ai.service.loader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import com.bfh.moduletracker.ai.config.PostgresTestcontainerConfiguration;
import com.bfh.moduletracker.ai.config.TestSecurityConfiguration;
import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import com.bfh.moduletracker.ai.service.module.ModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@Import({PostgresTestcontainerConfiguration.class, TestSecurityConfiguration.class})
@ActiveProfiles("inttest")
class ModuleLoaderIT {

    @Autowired
    ModuleService moduleService;

    @Autowired
    private ModuleRepository moduleRepository;

    @MockitoBean
    VectorStore vectorStore;

    @Test
    void on_applicationStart_withInitialExcelData_shouldLoad32ModulesToRelationalDb() {
        // Given
        // The application context has started and the CommandLineRunner has imported modules from the initial Excel file into the database

        // Act
        List<ModuleDto> modules = moduleService.getAllModules();

        // Then
        assertThat(modules)
                .isNotEmpty()
                .hasSize(40);
    }

    @Test
    void firstModuleFromExcel_shouldHaveId550_andCodeBSS1203_37() {
        // Given
        String expectedId = "550.0";
        String expectedCode = "BSS1203.37";

        // When
        Module first = moduleRepository.findById(expectedId).orElseThrow();

        // Then
        assertThat(first.getCode()).isEqualTo(expectedCode);
    }

    @Test
    void lastModuleFromExcel_shouldHaveId4_andCodeWI102() {
        // Given
        String expectedId = "4.0";
        String expectedCode = "WI102";

        // When

        Module last = moduleRepository.findById(expectedId).orElseThrow();

        // Then
        assertThat(last.getCode()).isEqualTo(expectedCode);
    }
}
