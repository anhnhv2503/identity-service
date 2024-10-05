package com.anhnhvcoder.devteria.config;

import com.anhnhvcoder.devteria.enums.ROLE;
import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.repository.RoleRepository;
import com.anhnhvcoder.devteria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                // Add some default users
                var role = roleRepository.findByName(ROLE.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("Admin")
                        .lastName("Admin")
                        .email("prjonlineshop@gmail.com")
                        .isActive(true)
                        .dob(LocalDate.of(2003, 3, 25))
                        .roles(Set.of(role))
                        .build();
                userRepository.save(user);

                log.warn("Default user created: admin");
            }
        };
    }
}
