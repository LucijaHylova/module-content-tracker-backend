package com.bfh.moduletracker.ai.model.dto.ai.module_attributes;

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
public class DepartmentDto {
    private String de;
    private String en;
    private String fr;
}
