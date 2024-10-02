package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.request.UserDTO;
import com.anhnhvcoder.devteria.enums.ROLE;
import com.anhnhvcoder.devteria.exception.AppException;
import com.anhnhvcoder.devteria.exception.ErrorCode;
import com.anhnhvcoder.devteria.mapper.UserMapper;
import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.repository.RoleRepository;
import com.anhnhvcoder.devteria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDTO addUser(User user) {

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var role = roleRepository.findByName(ROLE.USER.name());
        user.setRoles(Set.of(role));

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
    @PostAuthorize("returnObject.username == authentication.name")
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

    @Override
    public UserDTO getMyProfile() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return UserMapper.mapUserToUserDTO(user);
    }

}
