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
public class Content {

    @Column(name = "content_de", length = 25500)
    String contentDe;

    @Column(name = "conetent_en", length = 25500)
    String contentEn;

    @Column(name = "content_fr", length = 25500)
    String contentFr;
}
