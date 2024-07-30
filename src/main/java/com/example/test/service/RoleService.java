package com.example.test.service;

import org.springframework.stereotype.Service;

import com.example.test.domain.Role;
import com.example.test.domain.Permission;
import java.util.List;
import java.util.stream.Collectors;

import com.example.test.repository.PermissionRepository;
import com.example.test.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role create(Role role) {
        if (role.getPermissions() != null) {
            List<Permission> permissions = role.getPermissions();
            List<Long> permissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());
            List<Permission> permissionsInDB = this.permissionRepository.findByIdIn(permissionIds);
            role.setPermissions(permissionsInDB);
        }

        return this.roleRepository.save(role);

    }

    public Role getRoleById(long id) {
        return this.roleRepository.findById(id);
    }
}
