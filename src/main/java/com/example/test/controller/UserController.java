package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.test.domain.User;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseUserDTO;
import com.example.test.service.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.test.core.error.BadRequestException;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {
        ResponsePaginationDTO users = this.userService.getAllUsers(spec, pageable);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) throws BadRequestException {

        ResponseUserDTO createdUser = this.userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) throws BadRequestException {

        this.userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable long id) throws BadRequestException {

        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);

    }

}
