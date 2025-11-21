package com.bfh.moduletracker.ai.repository;

import com.bfh.moduletracker.ai.model.entity.ModuleContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleContentRepository extends JpaRepository<ModuleContent, String> {
    ModuleContent findByCode(String moduleCode);

}

