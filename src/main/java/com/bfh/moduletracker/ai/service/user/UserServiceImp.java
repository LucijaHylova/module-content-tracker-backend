package com.bfh.moduletracker.ai.service.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.exceptions.InvalidUserModificationException;
import com.bfh.moduletracker.ai.model.dto.user.UserResponseDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserUpdateRequestDto;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import com.bfh.moduletracker.ai.repository.UserModuleRepository;
import com.bfh.moduletracker.ai.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final UserModuleRepository userModuleRepository;

    public UserServiceImp(UserRepository userEntityRepository, ModuleRepository moduleRepository, UserModuleRepository userModuleRepository) {
        this.userRepository = userEntityRepository;
        this.moduleRepository = moduleRepository;
        this.userModuleRepository = userModuleRepository;
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return GeneralMapper.mapToUserResponseDto(user);
    }


    public List<UserResponseDto> getAllUsers() {
        log.atInfo().log("Listing all users");
        return this.userRepository.findAll().stream()
                .map(GeneralMapper::mapToUserResponseDto)
                .toList();
    }

    public List<User> getAllUserDetails() {
        log.atInfo().log("Listing all user details");
        return this.userRepository.findAll().stream()
                .toList();
    }


    @Override
    @Transactional
    public void updateUser(UserUpdateRequestDto userUpdateRequest) throws InvalidUserModificationException {
        if (
                userUpdateRequest.getUsername() == null) {
            throw new InvalidUserModificationException("Missing username or module code in request.");
        }

        String username = userUpdateRequest.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found: %s", username)));


        if (userUpdateRequest.getSpecialization() != null) {

            user.setSpecialization(userUpdateRequest.getSpecialization());

            Set<Module> modules = this.moduleRepository.getCompulsoryModulesByProgram(user.getProgram(), userUpdateRequest.getSpecialization());

            Set<UserModule> userModules = modules.stream()
                    .map(m -> {
                        return UserModule.builder()
                                .id(GeneralMapper.mapToModuleUserId(user.getUsername(), m.getCode()))
                                .user(user)
                                .module(m)
                                .selectedSemester(!m.getSemesters().isEmpty() ? m.getSemesters().getFirst() : 0)
                                .build();

                    })
                    .collect(Collectors.toSet());

            this.userModuleRepository.saveAll(userModules);

            Set<UserModule> existingUserModules =
                    new HashSet<>(userModuleRepository.findAllByUsername(username));

            Set<String> compulsoryCodes = modules.stream()
                    .map(Module::getCode)
                    .collect(Collectors.toSet());

            Set<UserModule> modulesToDelete = existingUserModules.stream()
                    .filter(um ->
                            um.getModule().getModuleType().getModuleTypeDe().equals("Pflichtmodul")
                                    && !compulsoryCodes.contains(um.getModule().getCode()))
                    .collect(Collectors.toSet());

            modulesToDelete.forEach(um -> {
                user.getUserModules().remove(um);
            });

        }

        if (userUpdateRequest.getTotalPassedEcts() != null) {
            user.setTotalPassedEcts(userUpdateRequest.getTotalPassedEcts());
        }
    }



    @Transactional
    public void deleteUser(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        log.atInfo().log("Deleting user {}", username);

        this.userRepository.deleteByUsername(user.getUsername());
    }


}
