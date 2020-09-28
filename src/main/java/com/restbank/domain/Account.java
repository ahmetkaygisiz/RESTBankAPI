package com.restbank.domain;

import com.restbank.domain.annotation.MinValueZero;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Set;

/**
 * <h2><b>Account class</b> contains users account infos</h2>
 *
 * <p>
 *     User,Transaction list and Credit Card classes also related in this class.
 *
 *     <b>Relations :</b>
 *          User  ( 1 ) -> (N) Account    : user_accounts table
 *          CreditCard (1) -> (1) Account : keeping in a row each table.
 *          Transaction (N) -> (1) Account : keeeping transactions_table
 * </p>
 */
@Data
@Entity
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Long id;

    // 8 basamakli bir ucret olmali.
    @NotNull
    @Pattern(regexp = "^\\d{8}$")
    private String accountNumber;

    // min değer 0.0 olmali Pattern for String regexp = ^([0-9]*[.])?\d{2}
     @MinValueZero
     private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable( name = "user_accounts",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_t_list")
    private Set<Transaction> transactionList;

    @OneToOne
    private CreditCard creditCard;
}
