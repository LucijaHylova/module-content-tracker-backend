package com.bfh.moduletracker.ai.controller;

import com.bfh.moduletracker.ai.exceptions.InvalidModuleAssignmentException;
import com.bfh.moduletracker.ai.exceptions.UserModuleAlreadyExistException;
import com.bfh.moduletracker.ai.model.dto.RequestResponseDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleIdDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleSelectionRequestDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleUpdateRequestDto;
import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.model.enums.Status;
import com.bfh.moduletracker.ai.service.userModule.UserModuleService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userModules")
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "1.0", description = "Authentication and Authorization API"))
public class UserModuleController {

    private static final Logger log = LoggerFactory.getLogger(UserModuleController.class);
    private final UserModuleService userModuleService;


    public UserModuleController(UserModuleService userModuleService) {
        this.userModuleService = userModuleService;

    }


    @PutMapping("/me/addUserModule")
    public ResponseEntity<RequestResponseDto> addModuleToUser(
            @RequestBody UserModuleSelectionRequestDto moduleSelectionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            this.userModuleService.addModuleToUser(moduleSelectionRequest);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(RequestResponseDto.builder()
                            .message(String.format("User '%s' not found.", username))
                            .build());

        } catch (InvalidModuleAssignmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(RequestResponseDto.builder()
                            .message(String.format("Invalid module assignment: %s", e.getMessage()))
                            .build());
        } catch (UserModuleAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(RequestResponseDto.builder()
                            .message(String.format("Module modification error: %s", e.getMessage()))
                            .build());
        }

        return ResponseEntity.ok(RequestResponseDto.builder().message(
                String.format("Module %s for %s semester was added successfully", moduleSelectionRequest.getId().getModuleCode(), moduleSelectionRequest.getSelectedSemester())).build());
    }

    @PutMapping("/me/updateUserModule")
    public ResponseEntity<RequestResponseDto> modifyUserModule(
            @RequestBody UserModuleUpdateRequestDto userModuleUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        String  newStatus = userModuleUpdateRequest.getStatus();
        Integer newSemester = userModuleUpdateRequest.getSelectedSemester();

        boolean semesterProvided = newSemester != null;


        if (semesterProvided &&
                (Status.FAILED.getDisplayName().equals(newStatus) || Status.PASSED.getDisplayName().equals(newStatus) || Status.RETAKE_IN_PROGRESS.equals(newStatus)
                )) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RequestResponseDto.builder()
                            .message("Semester is not allowed to be set when status is PASSED or FAILED.")
                            .build());
        }

        try {
            this.userModuleService.updateUserModule(userModuleUpdateRequest);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(RequestResponseDto.builder()
                            .message(String.format("User '%s' not found.", username))
                            .build());

        } catch (InvalidModuleAssignmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(RequestResponseDto.builder()
                            .message(String.format("Invalid module modification: %s", e.getMessage()))
                            .build());
        }


        return ResponseEntity.ok(RequestResponseDto.builder().message(
                String.format("Module %s was updated successfully", userModuleUpdateRequest.getId())).build());
    }


    @DeleteMapping("/me/deleteUserModule")
    public ResponseEntity<RequestResponseDto> deleteModuleToUser(
            @RequestBody UserModuleIdDto userModuleId) {

        try {
            this.userModuleService.deleteUserModule(userModuleId);
        } catch (InvalidModuleAssignmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(RequestResponseDto.builder()
                            .message(String.format("Invalid module code: %s", e.getMessage()))
                            .build());
        }

        return ResponseEntity.ok(RequestResponseDto.builder().message(
                String.format("Module %s for was deleted successfully", userModuleId.getModuleCode())).build());
    }


}
