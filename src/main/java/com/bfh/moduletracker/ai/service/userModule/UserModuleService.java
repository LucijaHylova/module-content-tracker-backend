
package com.bfh.moduletracker.ai.service.userModule;

import com.bfh.moduletracker.ai.exceptions.InvalidModuleAssignmentException;
import com.bfh.moduletracker.ai.exceptions.UserModuleAlreadyExistException;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleIdDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleSelectionRequestDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserModuleUpdateRequestDto;

public interface UserModuleService {


    void addModuleToUser(UserModuleSelectionRequestDto moduleSelectionRequestDto) throws InvalidModuleAssignmentException, UserModuleAlreadyExistException;


    void deleteUserModule(UserModuleIdDto userModuleId) throws InvalidModuleAssignmentException;

    void updateUserModule(UserModuleUpdateRequestDto userModuleUpdateRequest) throws InvalidModuleAssignmentException;

    public UserModuleDto getUserModule(UserModuleIdDto userModuleIdDto);


}



