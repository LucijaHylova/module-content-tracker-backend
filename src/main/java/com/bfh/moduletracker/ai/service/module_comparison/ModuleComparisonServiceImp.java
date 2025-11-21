package com.bfh.moduletracker.ai.service.module_comparison;

import java.util.ArrayList;
import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonDeleteRequest;
import com.bfh.moduletracker.ai.model.entity.ModuleComparison;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleComparisonServiceImp implements ModuleComparisonService {
    private static final Logger log = LoggerFactory.getLogger(ModuleComparisonServiceImp.class);

    private final UserRepository userRepository;


    public ModuleComparisonServiceImp(UserRepository userEntityRepository) {
        this.userRepository = userEntityRepository;
    }


    @Override
    @Transactional
    public void deleteModuleComparison(ModuleComparisonDeleteRequest moduleComparisonDeleteRequest, String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found: %s", username)));

        List<ModuleComparison> moduleComparisons = this.userRepository.findModuleComparisonsByUsername(username);
        List<ModuleComparison> toRemove = moduleComparisons.stream()
                .filter(mc -> mc.getCode().equals(moduleComparisonDeleteRequest.getCode()))
                .toList();

        user.getModuleComparisons().removeAll(toRemove);
        List<ModuleComparison> remaining = new ArrayList<>(user.getModuleComparisons());
        List<ModuleComparison> unique = new ArrayList<>();

        java.util.Set<String> seenCodes = new java.util.HashSet<>();

        for (ModuleComparison mc : remaining) {
            if (seenCodes.add(mc.getCode())) {
                unique.add(mc);
            }

        }

        user.setModuleComparisons(unique);

        this.userRepository.save(user);
    }
}
