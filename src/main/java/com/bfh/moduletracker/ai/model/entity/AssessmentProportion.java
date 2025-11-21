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
public class AssessmentProportion {

    @Column(name = "assessment_proportion_de", length = 25500)
    String assessmentProportionDe;

    @Column(name = "assessment_proportion_en", length = 25500)
    String assessmentProportionEn;

    @Column(name = "assessment_proportion_fr", length = 25500)
    String assessmentProportionFr;
}
