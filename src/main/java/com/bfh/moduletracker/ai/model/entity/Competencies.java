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
public class Competencies {

    @Column(name = "competencies_de", length = 25500)
    String competenciesDe;

    @Column(name = "competencies_en", length = 25500)
    String competenciesEn;

    @Column(name = "competencies_fr", length = 25500)
    String competenciesFr;
}
