
package com.bfh.moduletracker.ai.service.user;

import java.util.List;

import com.bfh.moduletracker.ai.exceptions.InvalidUserModificationException;
import com.bfh.moduletracker.ai.model.dto.user.UserResponseDto;
import com.bfh.moduletracker.ai.model.dto.usermodule.UserUpdateRequestDto;
import com.bfh.moduletracker.ai.model.entity.User;

public interface UserService {

    UserResponseDto getUserByUsername(String username);

    void deleteUser(String username);

    List<UserResponseDto> getAllUsers();

    List<User> getAllUserDetails();


    void updateUser(UserUpdateRequestDto userUpdateRequestDto) throws InvalidUserModificationException;

}



