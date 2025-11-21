package com.bfh.moduletracker.ai.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Department {

    @Column(name = "department_de", length = 25500)
    String departmentDe;

    @Column(name = "department_en", length = 25500)
    String departmentEn;

    @Column(name = "department_fr", length = 25500)
    String departmentFr;
}
