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

public class UserModuleSelectionRequestDto {
    private UserModuleIdDto id;
    private Integer selectedSemester;
    private String status;

}
