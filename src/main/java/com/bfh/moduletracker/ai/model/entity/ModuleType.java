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
public class ModuleType {

    @Column(name = "module_type_de", length = 25500)
    String moduleTypeDe;

    @Column(name = "module_type_en", length = 25500)
    String moduleTypeEn;

    @Column(name = "module_type_fr", length = 25500)
    String moduleTypeFr;
}
