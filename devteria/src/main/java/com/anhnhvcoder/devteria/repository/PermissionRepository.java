package com.anhnhvcoder.devteria.repository;

import com.anhnhvcoder.devteria.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String>{
}
