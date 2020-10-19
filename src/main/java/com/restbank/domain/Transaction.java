package com.restbank.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long id;

    @NotNull
    private String transferFrom;

    @NotNull
    private String transferTo;

    @NotNull
    private BigDecimal amount;

    private String description;

    private Date date = new Date();

    public Transaction(String transferFrom, String transferTo, String description, BigDecimal amount) {
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.description = description;
        this.amount = amount;
    }
}
