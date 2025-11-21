package com.bfh.moduletracker.ai.service.usermodule;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.exceptions.InvalidModuleAssignmentException;
import com.bfh.moduletracker.ai.exceptions.UserModuleAlreadyExistException;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleIdDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleSelectionRequestDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleUpdateRequestDto;
import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.model.entity.UserModuleId;
import com.bfh.moduletracker.ai.model.enums.ModuleTypeFilterTag;
import com.bfh.moduletracker.ai.repository.ModuleRepository;
import com.bfh.moduletracker.ai.repository.UserModuleRepository;
import com.bfh.moduletracker.ai.repository.UserRepository;
import com.bfh.moduletracker.ai.service.userModule.UserModuleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserModuleServiceImp implements UserModuleService {
    private static final Logger log = LoggerFactory.getLogger(UserModuleServiceImp.class);

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final UserModuleRepository userModuleRepository;


    public UserModuleServiceImp(UserRepository userEntityRepository, ModuleRepository moduleRepository, ModuleRepository moduleRepository1, UserModuleRepository userModuleRepository) {
        this.userRepository = userEntityRepository;
        this.moduleRepository = moduleRepository1;
        this.userModuleRepository = userModuleRepository;
    }

    @Override
    @Transactional
    public void addModuleToUser(UserModuleSelectionRequestDto moduleSelectionRequest) throws InvalidModuleAssignmentException, UserModuleAlreadyExistException {
        String username = moduleSelectionRequest.getId().getUsername();
        String moduleCode = moduleSelectionRequest.getId().getModuleCode();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found: %s", username)));

        Module module = moduleRepository.findModuleByCode(moduleCode)
                .orElseThrow(() -> new InvalidModuleAssignmentException(
                        String.format("Module %s not found.", moduleCode)));

        boolean isAllowed =
                "Wahlmodul (anrechenbar)".equals(module.getModuleType().getModuleTypeDe()) ||
                        module.getProgram().getProgramDe().equals(user.getProgram());

        if (!isAllowed) {
            throw new InvalidModuleAssignmentException(
                    String.format("Module %s cannot be assigned to user %s (program mismatch or not selectable).",
                            module.getCode(), user.getUsername()));
        }

        List<Integer> availableSemesters = module.getSemesters();
        if (!availableSemesters.contains(moduleSelectionRequest.getSelectedSemester())) {
            throw new InvalidModuleAssignmentException(
                    String.format("Selected semester %d is not available for module %s.",
                            moduleSelectionRequest.getSelectedSemester(), module.getCode()));
        }

        UserModuleId userModuleId = UserModuleId.builder()
                .moduleCode(module.getCode())
                .username(user.getUsername())
                .build();

        if (this.userModuleRepository.existsById(userModuleId)) {
            throw new UserModuleAlreadyExistException(
                    String.format("Module %s is already assigned to user %s.",
                            module.getCode(), user.getUsername()));
        }

        UserModule userModule = UserModule.builder()
                .id(userModuleId)
                .user(user)
                .module(module)
                .selectedSemester(moduleSelectionRequest.getSelectedSemester())
                .status(moduleSelectionRequest.getStatus())
                .build();

        this.userModuleRepository.save(userModule);

    }


    @Override
    @Transactional
    public void deleteUserModule(UserModuleIdDto userModuleId) throws InvalidModuleAssignmentException {

        User user = userRepository.findByUsername(userModuleId.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found: %s", userModuleId.getUsername())));

//
//        UserModule userModule = user.getUserModules().stream()
//                .filter(um -> um.getModule().getCode().equals(userModuleId.getModuleCode()))
//                .findFirst()
//                .orElseThrow(() -> new InvalidModuleAssignmentException(
//                        String.format("Module %s is not assigned to user %s.",
//                                userModuleId.getModuleCode(), userModuleId.getUsername())));
        Set<UserModule> usermodules = userModuleRepository.findAllByUsername(user.getUsername());
        var userModule = usermodules.stream()
                .filter(um -> um.getModule().getCode().equals(userModuleId.getModuleCode()))
                .findFirst().orElseThrow(() -> new InvalidModuleAssignmentException(
                String.format("Module %s is not assigned to user %s.", userModuleId.getModuleCode(), userModuleId.getUsername())));

        if (Objects.equals(userModule.getModule().getModuleType().getModuleTypeDe(),
                ModuleTypeFilterTag.PFLICHTMODUL.getTag())) {
            throw new InvalidModuleAssignmentException(
                    String.format("Module %s is a Pflichtmodul and cannot be removed from user %s.",
                            userModuleId.getModuleCode(), userModuleId.getUsername()));
        }
        this.userModuleRepository.delete(userModule);
    }

    @Override
    public UserModuleDto getUserModule(UserModuleIdDto dto) {

        UserModule um = userModuleRepository
                .findById(dto.getUsername(), dto.getModuleCode())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Module '" + dto.getModuleCode() + "' not found for user '" + dto.getUsername() + "'")
                );

        return GeneralMapper.mapToUserModuleDto(um);
    }


    @Override
    @Transactional
    public void updateUserModule(UserModuleUpdateRequestDto userModuleUpdateRequest) throws InvalidModuleAssignmentException {
        if (
                userModuleUpdateRequest.getId().getUsername() == null ||
                        userModuleUpdateRequest.getId().getModuleCode() == null) {
            throw new InvalidModuleAssignmentException("Missing username or module code in request.");
        }

        String username = userModuleUpdateRequest.getId().getUsername();
        String moduleCode = userModuleUpdateRequest.getId().getModuleCode();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found: %s", username)));

        Set<UserModule> usermodules = userModuleRepository.findAllByUsername(user.getUsername());
        var userModule = usermodules.stream()
                .filter(um -> um.getModule().getCode().equals(moduleCode))
                .findFirst().orElseThrow(() -> new InvalidModuleAssignmentException(
                String.format("Module %s is not assigned to user %s.", moduleCode, username)));

/*        UserModule userModule = user.getUserModules().stream()
                .filter(um -> um.getModule().getCode().equals(moduleCode))
                .findFirst()
                .orElseThrow(() -> new InvalidModuleAssignmentException(
                        String.format("Module %s is not assigned to user %s.", moduleCode, username)));*/


        if (userModuleUpdateRequest.getStatus() != null) {
            userModule.setStatus(userModuleUpdateRequest.getStatus());
        }

        if (userModuleUpdateRequest.getSelectedSemester() != null) {
            userModule.setSelectedSemester(userModuleUpdateRequest.getSelectedSemester());
        }

        this.userModuleRepository.save(userModule);
    }
}
