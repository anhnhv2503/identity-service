package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.UserDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO addUser(UserDTO userDTO) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = UserMapper.mapUserDTOToUser(userDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return UserMapper.mapUserToUserDTO(user);
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
    public UserDTO updateUser(String id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);

        return UserMapper.mapUserToUserDTO(user);
    }

    @Override
    public void deleteUser(String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

}
