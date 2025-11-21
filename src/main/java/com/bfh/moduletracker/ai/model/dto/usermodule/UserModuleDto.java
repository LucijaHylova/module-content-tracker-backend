package com.bfh.moduletracker.ai.model.dto.usermodule;

import com.bfh.moduletracker.ai.model.dto.ai.module_attributes.NameDto;
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
public class UserModuleDto {
    private String code;

    private String username;

    private NameDto name;

    private Integer selectedSemester;

    private String status;
}
