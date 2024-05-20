package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO addUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(String id);

    UserDTO updateUser(String id, UserDTO userDTO);

    void deleteUser(String id);
}
