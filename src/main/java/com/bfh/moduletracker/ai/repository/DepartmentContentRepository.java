package com.bfh.moduletracker.ai.repository;

import java.util.Optional;

import com.bfh.moduletracker.ai.model.entity.DepartmentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentContentRepository extends JpaRepository<DepartmentContent, String> {

    Optional<DepartmentContent> findByDepartment(String department);
}

