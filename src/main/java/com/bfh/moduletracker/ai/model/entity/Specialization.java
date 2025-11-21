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
public class Specialization {

    @Column(name = "specialization_de", length = 25500)
    String specializationDe;

    @Column(name = "specialization_en", length = 25500)
    String specializationEn;

    @Column(name = "specialization_fr", length = 25500)
    String specializationFr;
}
