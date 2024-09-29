package com.anhnhvcoder.devteria.controller;

import com.anhnhvcoder.devteria.dto.response.ApiResponse;
import com.anhnhvcoder.devteria.dto.request.UserDTO;
import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/users")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class UserController {

    private final IUserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDTO> insertUser(@RequestBody @Valid User user) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();

        UserDTO dto = userService.addUser(user);
        apiResponse.setResult(dto);

        return apiResponse;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("User: {}", auth.getName());
        auth.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).forEach(log::info);

        List<UserDTO> dtos = userService.getAllUsers();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String id) {

        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getUserById(id));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id,@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getMyProfile() {
        return new ResponseEntity<>(userService.getMyProfile(), HttpStatus.OK);
    }
}
