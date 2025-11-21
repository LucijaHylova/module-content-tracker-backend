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
public class Requirements {

    @Column(name = "requirements_de", length = 25500)
    String requirementsDe;

    @Column(name = "requirements_en", length = 25500)
    String requirementsEn;

    @Column(name = "requirements_fr", length = 25500)
    String requirementsFr;
}
