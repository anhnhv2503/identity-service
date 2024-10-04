package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.request.UserDTO;
import com.anhnhvcoder.devteria.model.User;
import jakarta.mail.MessagingException;

import java.util.List;

public interface IUserService {

    UserDTO addUser(User user) throws MessagingException;

    List<UserDTO> getAllUsers();

    UserDTO getUserById(String id);

    UserDTO updateUser(String id, User User);

    void deleteUser(String id);

    UserDTO getMyProfile();
}
