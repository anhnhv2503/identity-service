package com.anhnhvcoder.devteria.repository;

import com.anhnhvcoder.devteria.model.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String>{
}
