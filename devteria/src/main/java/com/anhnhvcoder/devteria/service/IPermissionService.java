package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.model.Permission;

import java.util.List;

public interface IPermissionService {

    Permission createPermission(Permission permission);

    List<Permission> getAllPermissions();

    void deletePermission(String name);
}
