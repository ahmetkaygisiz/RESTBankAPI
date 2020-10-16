package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restbank.domain.annotation.UniqueRoleName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(unique = true)
    @UniqueRoleName
    private String name;

    @OneToMany(mappedBy = "role", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    private Set<UserRole> userRoles =  new HashSet<>();
}
