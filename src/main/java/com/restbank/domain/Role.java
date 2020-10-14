package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restbank.domain.annotation.UniqueRoleName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(unique = true)
    @UniqueRoleName
    private String name;


}
