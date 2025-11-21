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
public class Responsibility {

    @Column(name = "responsibility_de", length = 25500)
    String responsibilityDe;

    @Column(name = "responsibility_en", length = 25500)
    String responsibilityEn;

    @Column(name = "responsibility_fr", length = 25500)
    String responsibilityFr;
}
