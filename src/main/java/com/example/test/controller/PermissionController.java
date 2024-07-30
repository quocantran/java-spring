package com.example.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Permission;
import com.example.test.service.PermissionService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission permission) throws BadRequestException {
        Boolean isExist = this.permissionService.existsByNameAndApiPathAndMethod(permission.getName(),
                permission.getApiPath(), permission.getMethod());

        if (isExist) {
            throw new BadRequestException("Permission is already exist");
        }

        Permission entity = this.permissionService.create(permission);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }
}
