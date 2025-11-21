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
public class SelfStudy {

    @Column(name = "self_study_de")
    String selfStudyDe;

    @Column(name = "self_study_en")
    String selfStudyEn;

    @Column(name = "self_study_fr")
    String selfStudyFr;
}
