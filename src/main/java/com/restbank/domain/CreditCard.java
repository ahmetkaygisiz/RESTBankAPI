package com.restbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restbank.domain.annotation.TwoDigitsAfterPoint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <h2>Credit Card</h2>
 *
 * <p>
 *     <b>Relations :</b>
 *
 *     CreditCard (1) -> (1) Account : keeping in a row each table.
 * </p>
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_cart_id")
    private Long id;

    @NotNull
    @Column(length = 4)
    @Pattern(message="{restbankapi.constraints.creditcard.Length.message}", regexp="^(\\s*|\\d{4})$")
    private String bankCode;

    @NotNull
    @Column(length = 4) // for DB column
    @Pattern(message="{restbankapi.constraints.creditcard.Length.message}", regexp="^(\\s*|\\d{4})$")
    private String branchCode;

    // May be ths must be String idk.
    private LocalDate expireDate; // Will be generated in CreditCardService before the save

    @Column(length = 3)
    @Pattern(message="{restbankapi.constraints.creditcard.Cvc.message}", regexp="^(\\s*|\\d{3})$")
    private String cvc; // Will be generated in CreditCardService before the save

    @TwoDigitsAfterPoint
    private BigDecimal maxLimit = new BigDecimal("500.00");

    @TwoDigitsAfterPoint
    private BigDecimal usedAmount = new BigDecimal("00.00");

    @JsonIgnore
    @OneToOne(cascade = CascadeType.PERSIST ,fetch = FetchType.EAGER)
    private Account account;

    @Transient
    private String creditCardNumber;

    // credit card number connected with bankCode {4digit}, branch code {4digit} and id {8digit}.
    // I dont wnt to generate some random numbers for credit card. It must be unique like id.
    public String getCreditCardNumber(){
        this.creditCardNumber = (id != null ? bankCode + branchCode + String.format("%08d", id) : "");
        return creditCardNumber;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", bankCode='" + bankCode + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", expireDate=" + expireDate +
                ", cvc='" + cvc + '\'' +
                ", maxLimit=" + maxLimit +
                ", usedAmount=" + usedAmount +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                '}';
    }

}
