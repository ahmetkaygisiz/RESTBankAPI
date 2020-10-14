package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/1.0/users")
    public GenericResponse createUser(@Valid @RequestBody User user){
        userService.save(user);

        return new GenericResponse("User saved");
    }

    @GetMapping("/api/1.0/users")
    public GenericResponse getUserList(Pageable page){
        return userService.getUserVMList(page);
    }

    @GetMapping("/api/1.0/users/{id}")
    public GenericResponse getUserById(@PathVariable("id") Long id){
        return userService.getUserVMById(id);
    }

    @PutMapping("/api/1.0/users/{id}")
    public GenericResponse updateUser(@Valid @RequestBody User user){
        return userService.updateUser(user);
    }

    @PutMapping("/api/1.0/users/{id}/roles")
    public GenericResponse updateUserRoles(@PathVariable("id") Long id, @RequestParam String[] roles){
        return userService.updateRoles(id, roles);
    }
}
