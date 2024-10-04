package com.anhnhvcoder.devteria.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;
    private Set<String> roles;
}
