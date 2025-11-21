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
public class TeachingMethod {

    @Column(name = "teaching_method_de", length = 25500)
    String teachingMethodDe;

    @Column(name = "teaching_method_en", length = 25500)
    String teachingMethodEn;

    @Column(name = "teaching_method_fr", length = 25500)
    String teachingMethodFr;
}
