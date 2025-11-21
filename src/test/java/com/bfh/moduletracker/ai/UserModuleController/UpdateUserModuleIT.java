package com.bfh.moduletracker.ai.UserModuleController;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.bfh.moduletracker.ai.common.FakeUserEntityGenerator;
import com.bfh.moduletracker.ai.config.PostgresTestcontainerConfiguration;
import com.bfh.moduletracker.ai.config.TestSecurityConfiguration;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.ModuleType;
import com.bfh.moduletracker.ai.model.entity.Name;
import com.bfh.moduletracker.ai.model.entity.Program;
import com.bfh.moduletracker.ai.model.entity.Specialization;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.model.entity.UserModuleId;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import com.bfh.moduletracker.ai.repository.UserModuleRepository;
import com.bfh.moduletracker.ai.repository.UserRepository;
import com.bfh.moduletracker.ai.service.auth.JwtService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Transactional
class UpdateUserModuleIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserModuleRepository userModuleRepository;

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    private FakeUserEntityGenerator fakeUserEntityGenerator;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setup() {
        userModuleRepository.deleteAll();
        userRepository.deleteAll();
        moduleRepository.deleteAll();

        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("lucia", null));
    }

    private Module validModule(String code) {
        Module m = new Module();
        m.setCode(code);
        m.setProgram(Program.builder().programDe("BSc Informatik").build());
        m.setSpecialization(Specialization.builder().specializationDe("IT-Security").build());
        m.setModuleType(ModuleType.builder().moduleTypeDe("Pflichtmodul").build());
        m.setName(Name.builder().nameDe("Testmodul").build());
        m.setSemesters(List.of(1, 2, 3));
        m.setEcts("5");
        return moduleRepository.save(m);
    }

    @Test
    @Transactional
    void shouldUpdateUserModule_andReturn200() throws Exception {

        User user = userRepository.save(
                User.builder()
                        .username("lucia")
                        .email("lucia@example.com")
                        .password("x")
                        .program("BSc Informatik")
                        .specialization("IT-Security")
                        .enabled(true)
                        .build()
        );

        Module m = validModule("UPD100");

        userModuleRepository.save(new UserModule(
                new UserModuleId("lucia", "UPD100"),
                user,
                m,
                "Geplant",
                1
        ));
        entityManager.flush();

        String json = """
                {
                  "id": {
                    "username": "lucia",
                    "moduleCode": "UPD100"
                  },
                  "selectedSemester": 5,
                  "status": "In Bearbeitung"
                }
                """;

        mvc.perform(put("/userModules/me/updateUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        UserModule updated = userModuleRepository
                .findById(new UserModuleId("lucia", "UPD100"))
                .orElseThrow();

        assertThat(updated.getSelectedSemester()).isEqualTo(5);
        assertThat(updated.getStatus()).isEqualTo("In Bearbeitung");
    }

    @Test
    @Transactional
    void shouldReturn403_whenSettingSemesterForPassedOrFailed() throws Exception {

        userRepository.save(User.builder()
                .username("lucia")
                .email("l@e.com")
                .password("x")
                .program("BSc Informatik")
                .specialization("IT-Security")
                .enabled(true)
                .build());

        Module m = validModule("UPD200");

        userModuleRepository.save(new UserModule(
                new UserModuleId("lucia", "UPD200"),
                userRepository.findByUsername("lucia").get(),
                m,
                "In Bearbeitung",
                1
        ));

        String json = """
                {
                  "id": {
                    "username": "lucia",
                    "moduleCode": "UPD200"
                  },
                  "selectedSemester": 3,
                  "status": "Bestanden"
                }
                """;

        mvc.perform(put("/userModules/me/updateUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }


    @Test
    @Transactional
    void shouldReturn404_whenUserNotFound() throws Exception {

        Module m = validModule("UPD300");

        String json = """
                {
                  "id": {
                    "username": "ghost",
                    "moduleCode": "UPD300"
                  },
                  "selectedSemester": 2,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/updateUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }


    @Test
    @Transactional
    void shouldReturn400_whenModuleNotAssigned() throws Exception {

        userRepository.save(User.builder()
                .username("lucia")
                .email("l@e.com")
                .password("x")
                .program("BSc Informatik")
                .specialization("IT-Security")
                .enabled(true)
                .build());

        validModule("UPD400");

        String json = """
                {
                  "id": {
                    "username": "lucia",
                    "moduleCode": "UPD400"
                  },
                  "selectedSemester": 2,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/updateUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    void shouldReturn400_whenModuleDoesNotExist() throws Exception {

        userRepository.save(User.builder()
                .username("lucia")
                .email("l@e.com")
                .password("x")
                .program("BSc Informatik")
                .specialization("IT-Security")
                .enabled(true)
                .build());

        String json = """
                {
                  "id": {
                    "username": "lucia",
                    "moduleCode": "UNKNOWN"
                  },
                  "selectedSemester": 3,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/updateUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
