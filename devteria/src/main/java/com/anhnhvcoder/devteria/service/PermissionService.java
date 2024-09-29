package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.model.Permission;
import com.anhnhvcoder.devteria.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService{

    private final PermissionRepository permissionRepository;

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public void deletePermission(String name) {
        permissionRepository.deleteById(name);
    }
}
