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
public class ContactLessons {

    @Column(name = "contact_lesson_de", length = 25500)
    String contactLessonDe;

    @Column(name = "contact_lesson_en", length = 25500)
    String contactLessonEn;

    @Column(name = "contact_lesson_fr", length = 25500)
    String contactLessonFr;
}
