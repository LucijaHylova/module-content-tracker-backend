package com.bfh.moduletracker.ai.model.dto.usermodule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserModuleIdDto {
    private String moduleCode;
    private String username;
}
