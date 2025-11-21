package com.bfh.moduletracker.ai.model.dto.user;

import java.util.List;
import java.util.Set;

import com.bfh.moduletracker.ai.model.auth.ModuleComparisonDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleDto;
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
public class UserResponseDto {

    private String username;

    private String email;

    private String program;

    private Set<UserModuleDto> userModules;

    private String specialization;

    private List<ModuleComparisonDto> moduleComparisons;

    private Integer totalPassedEcts;


}
