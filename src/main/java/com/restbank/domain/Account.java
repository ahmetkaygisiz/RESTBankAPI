package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restbank.domain.annotation.TwoDigitsAfterPoint;
import com.restbank.domain.generator.AccountNumberGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * <h2><b>Account class</b> contains users account infos</h2>
 *
 * <p>
 *     User,Transaction list and Credit Card classes also related in this class.
 *
 *     <b>Relations :</b>
 *          User  ( 1 ) -> (N) Account    : user_accounts table
 *          CreditCard (1) -> (1) Account : keeping in a row each table.
 * </p>
 */
@Getter
@Setter
@Entity
@Table
public class Account {
    // PROBLEM : Increment param doesnt work
    @Id
    @Column(name = "account_id", updatable = false, insertable = false)
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private CreditCard creditCard;
}
