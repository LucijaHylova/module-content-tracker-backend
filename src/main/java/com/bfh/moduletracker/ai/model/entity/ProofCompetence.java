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
public class ProofCompetence {

    @Column(name = "proof_competence_de", length = 25500)
    String proofCompetenceDe;

    @Column(name = "proof_competence_en", length = 25500)
    String proofCompetenceEn;

    @Column(name = "proof_competence_fr", length = 25500)
    String proofCompetenceFr;
}
