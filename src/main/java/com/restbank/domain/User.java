package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restbank.domain.annotation.UniqueEmail;
import com.restbank.domain.annotation.UniquePhoneNumber;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import javax.persistence.*;
import java.util.*;

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

    @NotNull(message = "{restbankapi.constraints.firstName.NotNull.message}")
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull(message = "{restbankapi.constraints.lastName.NotNull.message}")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull(message = "{restbankapi.constraints.email.NotNull.message}")
    @Pattern(regexp = "^(.+)@(.+)$", message = "{restbankapi.constraints.email.Pattern.message}")
    @UniqueEmail
    private String email;

    @NotNull
    @Size(min = 8, max = 255)
    @Pattern( regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$" , message = "{restbankapi.constraints.password.Pattern.message}")
    private String password;

    @NotNull
    @UniquePhoneNumber
    @Pattern(regexp = "^\\d{10}$", message = "{restbankapi.constraints.phoneNumber.Pattern.message}")
    private String phoneNumber;

    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "users_roles",
                        joinColumns = @JoinColumn(name = "user_id"),
                        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles =  new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Account> accountList;

}
