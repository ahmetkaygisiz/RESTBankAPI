package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.User;
import com.restbank.domain.annotation.OnCreate;
import com.restbank.domain.annotation.OnUpdate;
import com.restbank.service.UserService;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(Statics.API_1_0_USERS)
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public GenericResponse createUser(@Validated(OnCreate.class) @RequestBody User user){
        userService.create(user);

        return new GenericResponse("User saved");
    }

    @GetMapping
    public GenericResponse getUserList(Pageable page){
        return userService.getUserVMList(page);
    }

    @GetMapping("/{id}")
    public GenericResponse getUserById(@PathVariable("id") Long id){
        return userService.getUserVMById(id);
    }

    @PutMapping("/{id}")
    public GenericResponse updateUser(@PathVariable("id") Long id,
                                      @Validated(OnUpdate.class) @RequestBody User user) throws InvocationTargetException, IllegalAccessException {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public GenericResponse deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/roles")
    public GenericResponse updateUserRoles(@PathVariable("id") Long id, @RequestParam String[] roles){
        return userService.updateRoles(id, roles);
    }

    @PutMapping("/{id}/accounts")
    public GenericResponse addUserAccounts(@PathVariable("id") Long id, @Valid @RequestBody Account account){
        return userService.addUserAccount(id, account);
    }

    @GetMapping("/{id}/accounts")
    public GenericResponse addUserAccounts(@PathVariable("id") Long id){
        return userService.getUserAccounts(id);
    }

}
