package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.request.RoleRequest;
import com.anhnhvcoder.devteria.model.Role;
import com.anhnhvcoder.devteria.repository.PermissionRepository;
import com.anhnhvcoder.devteria.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Role createRole(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role getRoleByName(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
