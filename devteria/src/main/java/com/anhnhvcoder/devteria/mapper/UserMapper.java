package com.anhnhvcoder.devteria.mapper;

import com.anhnhvcoder.devteria.dto.request.UserDTO;
import com.anhnhvcoder.devteria.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static User mapUserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());
        //user.setRoles(userDTO.getRoles());

        return user;
    }

    public static UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDob(user.getDob());
        userDTO.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));

        return userDTO;
    }
}
