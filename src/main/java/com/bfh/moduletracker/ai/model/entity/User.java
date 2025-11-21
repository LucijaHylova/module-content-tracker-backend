package com.bfh.moduletracker.ai.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bfh.moduletracker.ai.model.enums.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;


    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "SPECIALIZATION")
    private String specialization;

    @Builder.Default
    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "MODULE_COMPARISONS", joinColumns = @JoinColumn(name = "USERNAME"))
    private List<ModuleComparison> moduleComparisons = new ArrayList<>();

    @Column(name = "TOTAL_PASSED_ECTS")
    private Integer totalPassedEcts;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERMISSION_PROFILE")
    private Set<Roles> roles;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "PROGRAM")
    private String program;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserModule> userModules = new HashSet<>();

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setModuleComparisons(List<ModuleComparison> list) {
        this.moduleComparisons = (list == null) ? new ArrayList<>() : new ArrayList<>(list);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }
}
