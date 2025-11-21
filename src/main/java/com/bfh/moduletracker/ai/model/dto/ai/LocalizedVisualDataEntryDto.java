package com.bfh.moduletracker.ai.model.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LocalizedVisualDataEntryDto {
    private String key_de;
    private String key_en;
    private double value;
}

