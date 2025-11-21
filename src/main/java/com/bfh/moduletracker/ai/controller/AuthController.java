package com.bfh.moduletracker.ai.controller;

import java.time.Duration;

import com.bfh.moduletracker.ai.exceptions.UserAlreadyExistException;
import com.bfh.moduletracker.ai.model.auth.RegisterUserRequestDto;
import com.bfh.moduletracker.ai.model.auth.RegisterUserResponseDto;
import com.bfh.moduletracker.ai.model.dto.auth.LoginRequestDto;
import com.bfh.moduletracker.ai.service.auth.AuthService;
import com.bfh.moduletracker.ai.service.auth.JwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "1.0", description = "Authentication and Authorization API"))
public class AuthController {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<HttpStatus> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {

        var authentication = this.authService.authenticate(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateJwtToken(authentication);


        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofMinutes(this.jwtService.getExpirationTime()))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletResponse response) throws ServletException {
        ResponseCookie deleteAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(
            @RequestBody RegisterUserRequestDto userRequestDto) {


        RegisterUserResponseDto response = RegisterUserResponseDto.builder()
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .program(userRequestDto.getProgram())
                .build();

        try {
            authService.registerUser(userRequestDto);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

}
