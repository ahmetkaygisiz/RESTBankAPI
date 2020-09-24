package com.restbank.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <h2>User</h2>
 *
 * <p>
 *     <b>Relations :</b>
 *          User  (1)   -> (N) Account
 *          Role  (N)   -> (N) User
 * </p>
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;

    @NotNull
    @Size(min = 8, max = 255)
    @Pattern( regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$" )
    private String password;

    @NotNull
    @Pattern(regexp = "^\\d{10}$")
    private String phoneNumber;

    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "users_roles",
                        joinColumns = @JoinColumn(name = "user_id"),
                        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles =  new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Account> accountList;
}