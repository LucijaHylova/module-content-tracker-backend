package com.bfh.moduletracker.ai.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeachingForm {

    @Column(name = "teaching_form_de", length = 25500)
    String teachingFormDe;

    @Column(name = "teaching_form_en", length = 25500)
    String teachingFormEn;

    @Column(name = "teaching_form_fr", length = 25500)
    String teachingFormFr;
}
