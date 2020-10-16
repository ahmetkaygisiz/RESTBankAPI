package com.restbank.domain;

import com.restbank.domain.annotation.TwoDigitsAfterPoint;
import com.restbank.domain.generator.AccountNumberGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    // PROBLEM : Increment param doesnt work
    @Id
    @Column(name = "account_id")
    @Pattern(regexp = "^\\d{8}$")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountNumber_generator")
    @GenericGenerator(
            name = "accountNumber_generator",
            strategy = "com.restbank.domain.generator.AccountNumberGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = AccountNumberGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d"),
                    @org.hibernate.annotations.Parameter(name = AccountNumberGenerator.INCREMENT_PARAM, value = "50")
            }
    )
    private String accountNumber;

    // min deger 0.0 olmali Pattern for String regexp = ^([0-9]*[.])?\d{2}
     @TwoDigitsAfterPoint
     @DecimalMin(value = "0.0", message= "{restbankapi.constraints.balance.MinValueZero.message}")
     private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable( name = "user_accounts",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_t_list")
    private List<Transaction> transactionList = new ArrayList<>();

    @OneToOne(cascade=CascadeType.ALL)
    private CreditCard creditCard;
}
