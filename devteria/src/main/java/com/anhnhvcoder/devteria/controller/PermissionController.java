package com.anhnhvcoder.devteria.controller;

import com.anhnhvcoder.devteria.dto.response.ApiResponse;
import com.anhnhvcoder.devteria.model.Permission;
import com.anhnhvcoder.devteria.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.createPermission(permission));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deletePermission(@PathVariable String name) {
        permissionService.deletePermission(name);
        return ResponseEntity.ok(ApiResponse.builder().code(200).message("Permission deleted successfully").build());
    }
}
