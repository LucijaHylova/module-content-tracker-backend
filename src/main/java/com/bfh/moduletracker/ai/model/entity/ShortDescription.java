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
public class ShortDescription {

    @Column(name = "short_description_de", length = 25500)
    String shortDescriptionDe;

    @Column(name = "short_description_en", length = 25500)
    String shortDescriptionEn;

    @Column(name = "short_description_fr", length = 25500)
    String shortDescriptionFr;
}
