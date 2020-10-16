package com.restbank.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restbank.domain.Account;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.domain.annotation.UniqueEmail;
import com.restbank.domain.annotation.UniquePhoneNumber;
import com.restbank.utils.Statics;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserVM {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private String roles;

    public UserVM(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.active = user.isActive();
        //this.roles = user.getUserRoles().stream().map(Role::getName).collect(Collectors.joining(","));
    }
}
