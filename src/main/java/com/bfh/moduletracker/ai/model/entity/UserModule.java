package com.bfh.moduletracker.ai.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "USER_MODULES")
public class UserModule {

    @EmbeddedId
    private UserModuleId id;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "USERNAME")
    private User user;

    @ManyToOne
    @MapsId("moduleCode")
    @JoinColumn(name = "CODE")
    private Module module;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SELECTED_SEMESTER")
    private Integer selectedSemester;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModule that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
