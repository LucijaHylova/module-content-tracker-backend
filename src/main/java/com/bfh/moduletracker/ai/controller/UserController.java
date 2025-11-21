package com.bfh.moduletracker.ai.controller;

import java.util.List;

import com.bfh.moduletracker.ai.exceptions.InvalidUserModificationException;
import com.bfh.moduletracker.ai.model.dto.RequestResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonDeleteRequest;
import com.bfh.moduletracker.ai.model.dto.user.UserResponseDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserUpdateRequestDto;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.service.module_comparison.ModuleComparisonService;
import com.bfh.moduletracker.ai.service.user.UserDetailServiceImp;
import com.bfh.moduletracker.ai.service.user.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "1.0", description = "Authentication and Authorization API"))
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDetailsService userDetailService;
    private final ModuleComparisonService moduleComparisonService;


    public UserController(UserDetailServiceImp userDetailService, UserService userService, ModuleComparisonService moduleComparisonService) {
        this.userService = userService;
        this.userDetailService = userDetailService;
        this.moduleComparisonService = moduleComparisonService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        log.info("Fetching all users");
        val response = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all/details")
    public ResponseEntity<List<User>> getAllUserDetails() {
        log.info("Fetching all user details");
        val response = userService.getAllUserDetails();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            val response = this.userService.getUserByUsername(username);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/me/updateUser")
    public ResponseEntity<RequestResponseDto> updateUser(
            @RequestBody UserUpdateRequestDto userUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            this.userService.updateUser(userUpdateRequest);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(RequestResponseDto.builder()
                            .message(String.format("User '%s' not found.", username))
                            .build());

        } catch (InvalidUserModificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(RequestResponseDto.builder()
                            .message(String.format("Invalid user modification: %s", e.getMessage()))
                            .build());
        }


        return ResponseEntity.ok(RequestResponseDto.builder().message(
                String.format("User %s was updated successfully", userUpdateRequest.getUsername())).build());
    }

    @DeleteMapping("/module-comparison/delete")
    public ResponseEntity<RequestResponseDto> deleteModuleComparison(@RequestBody ModuleComparisonDeleteRequest moduleComparisonDeleteRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            this.moduleComparisonService.deleteModuleComparison(moduleComparisonDeleteRequest, username);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(RequestResponseDto.builder()
                            .message(String.format("User '%s' not found.", username))
                            .build());

        }

        return ResponseEntity.ok(RequestResponseDto.builder().message(String.format("Module comparisons for codes %s deleted successfully", moduleComparisonDeleteRequest)).build());

    }

    @GetMapping("/me/details")
    @Transactional(readOnly = true)
    public ResponseEntity<User> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User response = (User) authentication.getPrincipal();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/get/details/{username}")
    public ResponseEntity<UserDetails> getDetailsByUsername(@PathVariable String username) {
        try {
            UserDetails response = this.userDetailService.loadUserByUsername(username);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<UserResponseDto> getByUsername(@PathVariable String username) {
        try {
            val response = this.userService.getUserByUsername(username);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<RequestResponseDto> deleteUser(@PathVariable String username) {
        this.userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(RequestResponseDto.builder()
                .message(String.format("User %s deleted successfully", username))
                .build());
    }
}
