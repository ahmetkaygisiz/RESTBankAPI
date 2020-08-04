package com.restbank.domain;

import lombok.Data;

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

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

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
