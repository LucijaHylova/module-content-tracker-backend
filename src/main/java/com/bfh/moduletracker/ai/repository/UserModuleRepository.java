package com.bfh.moduletracker.ai.repository;

import java.util.Optional;
import java.util.Set;

import com.bfh.moduletracker.ai.model.entity.UserModule;
import com.bfh.moduletracker.ai.model.entity.UserModuleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, UserModuleId> {

    @Query("""
        SELECT um
        FROM UserModule um
        WHERE um.id.username = :username
          AND um.id.moduleCode = :moduleCode
    """)
    Optional<UserModule> findById(
            @Param("username") String username,
            @Param("moduleCode") String moduleCode);

    @Query("""
        SELECT um
        FROM UserModule um
        WHERE um.user.username = :username
    """)
    Set<UserModule> findAllByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM UserModule um WHERE um.id.username = :username")
    void deleteAllByIdUsername(String username);
}
