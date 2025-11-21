package com.bfh.moduletracker.ai.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bfh.moduletracker.ai.model.entity.Module;
import com.bfh.moduletracker.ai.model.entity.ModuleComparison;
import com.bfh.moduletracker.ai.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    @Query("SELECT u.userModules FROM User u WHERE u.username = :username")
    Set<Module> findModulesByUsername(@Param("username") String username);

    @Query("SELECT u.moduleComparisons FROM User u WHERE u.username = :username")
    List<ModuleComparison> findModuleComparisonsByUsername(@Param("username") String username);

}

