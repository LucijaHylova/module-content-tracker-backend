package com.bfh.moduletracker.ai.controller;

import com.bfh.moduletracker.ai.common.FakeUserEntityGenerator;
import com.bfh.moduletracker.ai.exceptions.InvalidModuleAssignmentException;
import com.bfh.moduletracker.ai.exceptions.UserAlreadyExistException;
import com.bfh.moduletracker.ai.exceptions.UserModuleAlreadyExistException;
import com.bfh.moduletracker.ai.repository.UserModuleRepository;
import com.bfh.moduletracker.ai.repository.UserRepository;
import org.hibernate.engine.spi.EntityEntryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/reset")
@Profile("test")
public class TestResetController {

    private final UserRepository userRepository;
    private final UserModuleRepository userModuleRepository;
    private final FakeUserEntityGenerator fakeUserEntityGenerator;

    public TestResetController(UserRepository userRepository, UserModuleRepository userModuleRepository, FakeUserEntityGenerator fakeUserEntityGenerator) {

        this.userRepository = userRepository;
        this.userModuleRepository = userModuleRepository;
        this.fakeUserEntityGenerator = fakeUserEntityGenerator;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> reset() throws UserAlreadyExistException, InvalidModuleAssignmentException, UserModuleAlreadyExistException {
        this.userRepository.deleteAll();
        this.userModuleRepository.deleteAll();
        this.fakeUserEntityGenerator.run("run");

        System.err.println("Test database has been reset.");
        return ResponseEntity.ok().build();
    }
}
