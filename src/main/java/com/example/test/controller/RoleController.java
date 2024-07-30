package com.example.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Role;
import com.example.test.service.RoleService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) throws BadRequestException {
        Boolean isExist = this.roleService.existsByName(role.getName());

        if (isExist) {
            throw new BadRequestException("Role is already exist");
        }

        Role entity = this.roleService.create(role);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }
}
