package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Role;
import com.restbank.service.RoleService;
import com.restbank.utils.Statics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    // GET MAPPING

    // PUT MAPPING

    // DELETE MAPPING
}
