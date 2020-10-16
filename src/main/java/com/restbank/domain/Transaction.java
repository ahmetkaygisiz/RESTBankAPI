package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <h2>CreditCard</h2>
 *
 * <p>
 *
 *     <b>Relations :</b>
 *          Transaction (N) -> (1) CreditCard : keeeping transactions_table
 *          Transaction (N) -> (1) Account : keeeping transactions_table
 * </p>
 */
@Data
@NoArgsConstructor
@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long id;

    private String description;

    private Date date = new Date();

    private BigDecimal amount;

    public Transaction(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
    }
}
