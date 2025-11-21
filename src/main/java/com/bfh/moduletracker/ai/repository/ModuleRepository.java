package com.bfh.moduletracker.ai.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bfh.moduletracker.ai.model.entity.Module;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {


    List<Module> findModulesByCode(String code, Sort sort);

    Optional<Module> findModuleByCode(String code);

    @Query("""
              SELECT m
              FROM Module m
              WHERE m.program.programDe = :program
                AND m.moduleType.moduleTypeDe = 'Pflichtmodul'
                AND (
                     m.specialization.specializationDe = :specialization
                     OR m.specialization.specializationDe = ''
                     OR m.specialization.specializationDe = 'no specialization'
                )
            """)
    Set<Module> getCompulsoryModulesByProgram(@Param("program") String program, @Param("specialization") String specialization);


}

