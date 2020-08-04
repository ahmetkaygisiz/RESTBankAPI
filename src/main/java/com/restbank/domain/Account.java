package com.restbank.domain;

import lombok.Data;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable( name = "user_accounts",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    private String accountNumber;

    private String cardNumber;

    private double balance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_t_list")
    private Set<Transaction> transactionList;

    @OneToOne
    private CreditCard creditCard;
}
