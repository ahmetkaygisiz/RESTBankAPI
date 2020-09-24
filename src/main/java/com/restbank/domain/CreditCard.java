package com.restbank.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * <h2>Credit Card</h2>
 *
 * <p>
 *     <b>Relations :</b>
 *
 *     CreditCard (1) -> (1) Account : keeping in a row each table.
 *     Transaction (N) -> (1) CreditCard : keeeping transactions_table
 * </p>
 */

@Data
@Entity
@Table
public class CreditCard {
    @Id
    @GeneratedValue
    @Column(name = "credit_cart_id")
    Long id;

    @OneToOne
    Account account;

    private Date expireDate;

    private char[] cvc;

    private double maxLimit;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_t_list")
    private Set<Transaction> transactionList;
}
