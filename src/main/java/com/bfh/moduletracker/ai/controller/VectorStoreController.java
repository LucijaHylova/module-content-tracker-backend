package com.bfh.moduletracker.ai.controller;

import java.util.List;

import com.bfh.moduletracker.ai.exceptions.InvalidUserModificationException;
import com.bfh.moduletracker.ai.model.dto.RequestResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonDeleteRequest;
import com.bfh.moduletracker.ai.model.dto.user.UserResponseDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserUpdateRequestDto;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.service.loader.VectorStoreLoad;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vectorStore")
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "1.0", description = "Authentication and Authorization API"))
public class VectorStoreController {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreController.class);
    private final VectorStoreLoad vectorStoreLoad;


    public VectorStoreController(VectorStoreLoad vectorStoreLoad) {

        this.vectorStoreLoad = vectorStoreLoad;
    }


    @PostMapping("/load")
    public ResponseEntity<Void> getAll() {
        log.info("Fetching all documents");
        vectorStoreLoad.run();
        return ResponseEntity.ok().build();
    }


}
