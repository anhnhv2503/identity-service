package com.anhnhvcoder.devteria;

import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DevteriaApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {

	}

}
