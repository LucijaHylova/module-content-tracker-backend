package com.bfh.moduletracker.ai.service.auth;

import java.util.Set;
import java.util.stream.Collectors;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.exceptions.UserAlreadyExistException;
import com.bfh.moduletracker.ai.model.auth.RegisterUserRequestDto;
import com.bfh.moduletracker.ai.model.dto.auth.LoginRequestDto;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.model.enums.Roles;
import com.bfh.moduletracker.ai.model.enums.Status;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import com.bfh.moduletracker.ai.repository.UserModuleRepository;
import com.bfh.moduletracker.ai.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImp.class);

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserModuleRepository userModuleRepository;


    public AuthServiceImp(UserRepository userEntityRepository, ModuleRepository moduleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserModuleRepository userModuleRepository) {
        this.userRepository = userEntityRepository;
        this.moduleRepository = moduleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userModuleRepository = userModuleRepository;
    }


    @Transactional
    @Override
    public void registerUser(RegisterUserRequestDto userRequestDto) throws UserAlreadyExistException {

        String specialization = userRequestDto.getSpecialization() == null || userRequestDto.getSpecialization().isEmpty()
                ? "no specialization"
                : userRequestDto.getSpecialization();
        userRequestDto.setSpecialization(specialization);

        Set<Module> modules = this.moduleRepository.getCompulsoryModulesByProgram(userRequestDto.getProgram(), userRequestDto.getSpecialization());

        userRepository.findByUsername(userRequestDto.getUsername())
                .ifPresent(u -> {
                    try {
                        throw new UserAlreadyExistException(
                                "Username " + userRequestDto.getUsername() + " already exists"
                        );
                    } catch (UserAlreadyExistException e) {
                        throw new RuntimeException(e);
                    }
                });



        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        User user = GeneralMapper.mapToUser(userRequestDto);
        user.setRoles(Set.of(Roles.USER_WITH_PROFILE));
        user.setEnabled(true);
        user.setTotalPassedEcts(0);
        this.userRepository.save(user);

        this.userModuleRepository.deleteAllByIdUsername(user.getUsername());

        Set<UserModule> userModules = modules.stream()
                .map(m -> {
                    return UserModule.builder()
                            .id(GeneralMapper.mapToModuleUserId(user.getUsername(), m.getCode()))
                            .user(user)
                            .module(m)
                            .selectedSemester(!m.getSemesters().isEmpty() ? m.getSemesters().getFirst() : 0)
                            .status(Status.PLANNED.getDisplayName())
                            .build();

                })
                .collect(Collectors.toSet());

        this.userModuleRepository.saveAll(userModules);
        user.setUserModules(userModules);
    }

    @Override
    public Authentication authenticate(LoginRequestDto request) {
        var authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return authentication;
    }
}
