package com.restbank.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long id;

    private String name;

    private String description;

    private Date date = new Date();

    private double amount;
}
