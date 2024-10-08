package com.anhnhvcoder.devteria.model;

import com.anhnhvcoder.devteria.validator.DobConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotEmpty(message = "Username is required")
    private String username;
    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must at least 8 characters")
    private String password;
    @Email(message = "Email is invalid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;
    @NotEmpty(message = "FirstName is required")
    private String firstName;
    @NotEmpty(message = "LastName is required")
    private String lastName;
    @DobConstraint(min = 11, message = "INVALID_DOB")
    private LocalDate dob;
    private boolean isActive;
    @ManyToMany
    private Set<Role> roles;

}
