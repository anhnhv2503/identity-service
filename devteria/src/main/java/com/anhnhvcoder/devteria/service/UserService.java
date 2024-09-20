package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.UserDTO;
import com.anhnhvcoder.devteria.enums.ROLE;
import com.anhnhvcoder.devteria.exception.AppException;
import com.anhnhvcoder.devteria.exception.ErrorCode;
import com.anhnhvcoder.devteria.mapper.UserMapper;
import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO addUser(User user) {

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(ROLE.USER.name());
        user.setRoles(roles);

        userRepository.save(user);

        UserDTO dto = UserMapper.mapUserToUserDTO(user);

        return dto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(UserMapper::mapUserToUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        return UserMapper.mapUserToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(String id, User user) {
        User thisUser = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        thisUser.setFirstName(user.getFirstName());
        thisUser.setLastName(user.getLastName());
        thisUser.setUsername(user.getUsername());
        thisUser.setPassword(user.getPassword());
        userRepository.save(user);

        return UserMapper.mapUserToUserDTO(user);
    }

    @Override
    public void deleteUser(String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

}
