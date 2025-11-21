package com.bfh.moduletracker.ai.UserModuleController;

import java.util.List;
import java.util.Optional;

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
class AddUserModuleIT {

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

    @BeforeEach
    void cleanUp() {
        userModuleRepository.deleteAll();
        userRepository.deleteAll();
        moduleRepository.deleteAll();

        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("lucia", null));
    }

    private Module createValidModule(String code) {
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
    void shouldAddUserModule_andReturn200() throws Exception {
        User user = User.builder()
                .username("lucia")
                .email("lucia@example.com")
                .password("x")
                .program("BSc Informatik")
                .specialization("IT-Security")
                .enabled(true)
                .build();
        userRepository.save(user);

        createValidModule("MT100");

        String json = """
                {
                  "id": {
                    "username": "lucia",
                    "moduleCode": "MT100"
                  },
                  "selectedSemester": 3,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/addUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        Optional<UserModule> saved = userModuleRepository.findById(
                new UserModuleId("lucia", "MT100")
        );

        assertThat(saved).isPresent();
        assertThat(saved.get().getSelectedSemester()).isEqualTo(3);
    }

    @Test
    @Transactional
    void shouldReturn404_whenUserNotFound() throws Exception {
        createValidModule("MT200");

        String json = """
                {
                  "id": {
                    "username": "ghost",
                    "moduleCode": "MT200"
                  },
                  "selectedSemester": 1,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/addUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldReturn400_whenModuleAssignmentInvalid() throws Exception {

        userRepository.save(User.builder()
                .username("testuser")
                .email("t@example.com")
                .password("x")
                .program("BSc Informatik")
                .specialization("IT-Security")
                .enabled(true)
                .build());

        String json = """
                {
                  "id": {
                    "username": "testuser",
                    "moduleCode": "UNKNOWN"
                  },
                  "selectedSemester": 2,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/addUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldReturn400_whenModuleAlreadyAssigned() throws Exception {

        User u = userRepository.save(
                User.builder()
                        .username("lucia")
                        .email("lucia@example.com")
                        .password("x")
                        .program("BSc Informatik")
                        .specialization("IT-Security")
                        .enabled(true)
                        .build()
        );

        Module m = createValidModule("MT300");


        userModuleRepository.save(
                new UserModule(new UserModuleId("lucia", "MT300"), u, m, "Geplant", 1)
        );

        String json = """
                {
                  "id": {
                    "username": "lucia",
                    "moduleCode": "MT300"
                  },
                  "selectedSemester": 1,
                  "status": "Geplant"
                }
                """;

        mvc.perform(put("/userModules/me/addUserModule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
