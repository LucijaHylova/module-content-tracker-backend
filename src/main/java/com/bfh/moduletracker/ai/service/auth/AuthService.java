package com.bfh.moduletracker.ai.service.auth;

import com.bfh.moduletracker.ai.exceptions.UserAlreadyExistException;
import com.bfh.moduletracker.ai.model.auth.RegisterUserRequestDto;
import com.bfh.moduletracker.ai.model.dto.auth.LoginRequestDto;
import org.springframework.security.core.Authentication;


public interface AuthService {

    void registerUser(RegisterUserRequestDto userRequestDto) throws UserAlreadyExistException;

    Authentication authenticate(LoginRequestDto request);
}
