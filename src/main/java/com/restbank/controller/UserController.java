package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.User;
import com.restbank.error.ApiError;
import com.restbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
    public GenericResponse getUserList(){
        return new GenericResponse(userService.getUserList().toString());
    }


}
