package com.bfh.moduletracker.ai.model.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Roles implements GrantedAuthority {
    ADMIN("ADMIN"),
    ADMIN_IMPORT("ADMIN_IMPORT"),
    USER_WITH_PROFILE("USER_WITH_PROFILE");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
