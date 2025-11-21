package com.bfh.moduletracker.ai.repository;

import java.util.Optional;

import com.bfh.moduletracker.ai.model.entity.ProgramContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramContentRepository extends JpaRepository<ProgramContent, String> {

    Optional<ProgramContent> findByProgram(String program);
}

