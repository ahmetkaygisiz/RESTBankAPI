package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.User;
import com.restbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public GenericResponse getUserList(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
        return userService.getUserList(pageNumber, pageSize);
    }

    // PUT MAPPING

    // DELETE MAPPING
}
