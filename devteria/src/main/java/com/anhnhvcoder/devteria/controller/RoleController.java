package com.anhnhvcoder.devteria.controller;

import com.anhnhvcoder.devteria.dto.request.RoleRequest;
import com.anhnhvcoder.devteria.dto.response.ApiResponse;
import com.anhnhvcoder.devteria.model.Role;
import com.anhnhvcoder.devteria.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final IRoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.builder().code(200).message("Role deleted successfully").build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleByName(id));
    }
}
