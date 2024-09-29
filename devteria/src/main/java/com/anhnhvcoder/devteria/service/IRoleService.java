package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.request.RoleRequest;
import com.anhnhvcoder.devteria.model.Role;

import java.util.List;

public interface IRoleService {

    Role createRole(RoleRequest request);

    List<Role> getAllRoles();

    void deleteRole(Long id);

    Role getRoleByName(Long id);
}
