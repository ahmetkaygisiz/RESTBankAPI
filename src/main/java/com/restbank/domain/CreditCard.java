package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.annotation.PostConstruct;
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

    private String bankCode;

    private String branchCode;

    private String cardNumber;

    private Date expireDate;

    private char[] cvc;

    private BigDecimal maxLimit; // asla limit yapma, Limit DB keyword'u

    private BigDecimal avaiableBalance; // Kullandikca Eksiye dusecek. Borc odedikce bu miktar artacak.

    @OneToOne
    Account account;

    @PostPersist
    public void postPersist(){
        String bankCode = "";
        String branchCode = "";
        String userCode = String.format("%08d", id);

        cardNumber = bankCode + branchCode + userCode;
    }
}
