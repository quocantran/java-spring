package com.example.test.service;

import org.springframework.stereotype.Service;

import com.example.test.domain.Permission;
import com.example.test.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Boolean existsByNameAndApiPathAndMethod(String name, String apiPath, String method) {
        return permissionRepository.existsByNameAndApiPathAndMethod(name, apiPath, method);
    }
}
