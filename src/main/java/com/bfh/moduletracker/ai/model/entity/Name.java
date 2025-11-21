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
public class Name {

    @Column(name = "name_de")
    String nameDe;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "name_fr")
    String nameFr;
}
