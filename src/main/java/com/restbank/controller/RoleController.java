package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Role;
import com.restbank.error.ApiError;
import com.restbank.repository.RoleRepository;
import com.restbank.service.RoleService;
import com.restbank.utils.Statics;
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
@RequestMapping(Statics.API_1_0_ROLES)
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping
    public GenericResponse createRole(@Valid @RequestBody Role role){
        roleService.saveRole(role);

        return new GenericResponse("Role Created");
    }

}
