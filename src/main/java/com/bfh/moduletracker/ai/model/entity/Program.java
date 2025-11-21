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
public class Program {

    @Column(name = "program_de", length = 25500)
    String programDe;

    @Column(name = "program_en", length = 25500)
    String programEn;

    @Column(name = "program_fr", length = 25500)
    String programFr;
}
