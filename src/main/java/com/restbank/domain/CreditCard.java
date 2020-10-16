package com.restbank.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_cart_id")
    private Long id;

    private char[] cardNumber;

    private Date expireDate;

    private char[] cvc;

    private BigDecimal maxLimit; // asla limit yapma, Limit DB keyword'u

    private BigDecimal avaiableBalance; // Kullandikca Eksiye dusecek. Borc odedikce bu miktar artacak.

    @OneToOne
    Account account;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_transaction_list")
    private List<Transaction> transactionList = new ArrayList<>();
}
