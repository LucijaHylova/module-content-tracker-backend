package com.bfh.moduletracker.ai.model.auth;

import java.util.List;

import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.NameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleComparisonDto {

    private String code;

    private NameDto name;

    // differences only
    private List<String> differences_de;
    private List<String> differences_en;

    // similarities only
    private List<String> similarities_de;
    private List<String> similarities_en;
}
