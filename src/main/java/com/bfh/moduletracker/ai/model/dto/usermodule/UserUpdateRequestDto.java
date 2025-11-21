package com.bfh.moduletracker.ai.model.dto.usermodule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserUpdateRequestDto {
    private String username;
    private String specialization;
    private Integer totalPassedEcts;
}
