package com.bfh.moduletracker.ai.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizedVisualDataEntry {

    @Column(name = "key_de")
    private String key_de;

    @Column(name = "key_en")
    private String key_en;

    @Column(name = "value")
    private double value;
}
