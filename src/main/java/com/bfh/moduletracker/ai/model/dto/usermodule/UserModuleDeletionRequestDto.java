package com.bfh.moduletracker.ai.model.dto.usermodule;

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

public class UserModuleDeletionRequestDto {
    private String moduleCode;
    private Integer selectedSemester;

}
