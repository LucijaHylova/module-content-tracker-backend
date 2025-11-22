package com.bfh.moduletracker.ai.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bfh.moduletracker.ai.exceptions.InvalidModuleAssignmentException;
import com.bfh.moduletracker.ai.exceptions.UserAlreadyExistException;
import com.bfh.moduletracker.ai.exceptions.UserModuleAlreadyExistException;
import com.bfh.moduletracker.ai.model.auth.RegisterUserRequestDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleIdDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleSelectionRequestDto;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.enums.Roles;
import com.bfh.moduletracker.ai.model.enums.Status;
import com.bfh.moduletracker.ai.repository.UserModuleRepository;
import com.bfh.moduletracker.ai.repository.UserRepository;
import com.bfh.moduletracker.ai.service.auth.AuthService;
import com.bfh.moduletracker.ai.service.userModule.UserModuleService;
import jakarta.transaction.Transactional;
import lombok.val;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class FakeUserGenerator {

    private static final Logger log = LoggerFactory.getLogger(FakeUserGenerator.class);

    @Value("${security.admin.username}")
    private String adminUsername;
    @Value("${security.admin.password}")
    private String adminPassword;

    @Value("${security.admin_import.username}")
    private String adminUserUsername;
    @Value("${security.admin_import.password}")
    private String adminUserPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserModuleService userModuleService;
    private final AuthService authService;

    public FakeUserGenerator(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserModuleService userService, AuthService authService, UserModuleRepository userModuleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userModuleService = userService;
        this.authService = authService;
    }


    @Transactional
    public void run() throws InvalidModuleAssignmentException, UserModuleAlreadyExistException, UserAlreadyExistException {
        userRepository.deleteAll();
        if (userRepository.count() != 0) {
            return;
        }

        Faker faker = new Faker();

        List<User> users = new ArrayList<>();

        users.add(User.builder()
                .username(adminUsername)
                .email("xxx@gmail.com")
                .password(passwordEncoder.encode(adminPassword))
                .roles(Set.of(Roles.ADMIN))
                .enabled(true)
                .build());

        users.add(User.builder()
                .username(adminUserUsername)
                .email("yyy@gmail.com")
                .password(passwordEncoder.encode(adminUserPassword))
                .roles(Set.of(Roles.ADMIN_IMPORT))
                .enabled(true)
                .build());

        RegisterUserRequestDto registerUser1Request = RegisterUserRequestDto.builder()
                .username("user1")
                .password("useruser")
                .email("yyy@gmail.com")
                .program("BSc Informatik")
                .specialization("IT-Security")
                .build();

        RegisterUserRequestDto registerUser11Request = RegisterUserRequestDto.builder()
                .username("user11")
                .password("useruser")
                .email("yyy@gmail.com")
                .program("BSc Informatik")
                .build();


        RegisterUserRequestDto registerUser111Request = RegisterUserRequestDto.builder()
                .username("user111")
                .password("useruser")
                .email("yyy@gmail.com")
                .program("BSc Informatik")
                .specialization("no specialization")
                .build();



        RegisterUserRequestDto registerUser2Request = RegisterUserRequestDto.builder()
                .username("user2")
                .password("useruser")
                .email("yyy@gmail.com")
                .program("Digital Business")
                .specialization("Digital Marketing")
                .build();

        RegisterUserRequestDto registerUser3Request = RegisterUserRequestDto.builder()
                .username("user3")
                .password("useruser")
                .email("yyy@gmail.com")
                .program("Wirtschaftsinformatik")
                .specialization("Software Engineering")
                .build();


        users.add(User.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(Roles.USER_WITH_PROFILE))
                .enabled(true)
                .build());

        userRepository.saveAll(users);
        this.authService.registerUser(registerUser1Request);
        this.authService.registerUser(registerUser2Request);
        this.authService.registerUser(registerUser3Request);
        this.authService.registerUser(registerUser11Request);
        this.authService.registerUser(registerUser111Request);


        val user1BTI2019 = UserModuleIdDto.builder()
                .username("user1")
                .moduleCode("BTI2019")
                .build();
        val  user11BTI2019 = UserModuleIdDto.builder()
                .username("user11")
                .moduleCode("BTI2019")
                .build();
        val user1BZG2302 = UserModuleIdDto.builder()
                .username("user1")
                .moduleCode("BZG2302")
                .build();
        val user1BTI2027 = UserModuleIdDto.builder()
                .username("user1")
                .moduleCode("BTI2027")
                .build();
        val user1BTM3825 = UserModuleIdDto.builder()
                .username("user1")
                .moduleCode("BTM3825")
                .build();
        val user11BTM3825 = UserModuleIdDto.builder()
                .username("user11")
                .moduleCode("BTM3825")
                .build();
        UserModuleSelectionRequestDto request1WahlpflichtModulUser1 = new UserModuleSelectionRequestDto(user1BTI2019, 3, Status.PLANNED.getDisplayName());
        UserModuleSelectionRequestDto request2WahlpflichtmodulUser1 = new UserModuleSelectionRequestDto(user1BZG2302, 4, Status.PLANNED.getDisplayName());
        UserModuleSelectionRequestDto request3WahlpflichtmodulUser1 = new UserModuleSelectionRequestDto(user1BTI2027, 4, Status.PLANNED.getDisplayName());
        UserModuleSelectionRequestDto request1WahlmodulUser1 = new UserModuleSelectionRequestDto(user1BTM3825, 6, Status.PLANNED.getDisplayName());
        UserModuleSelectionRequestDto request1WahlpflichtModulUser11 = new UserModuleSelectionRequestDto(user11BTI2019, 3, Status.PLANNED.getDisplayName());
        UserModuleSelectionRequestDto request1WahlmodulUser11 = new UserModuleSelectionRequestDto(user11BTM3825, 6, Status.PLANNED.getDisplayName());
        this.userModuleService.addModuleToUser(request1WahlpflichtModulUser1);
        this.userModuleService.addModuleToUser(request2WahlpflichtmodulUser1);
        this.userModuleService.addModuleToUser(request3WahlpflichtmodulUser1);
        this.userModuleService.addModuleToUser(request1WahlmodulUser1);
        this.userModuleService.addModuleToUser(request1WahlpflichtModulUser11);
        this.userModuleService.addModuleToUser(request1WahlmodulUser11);


        log.info("User entities seeded successfully.");
    }
}
