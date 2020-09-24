package com.restbank.repository;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByAccountNumber(String accountNumber);
    Account findByUser(User user);
    Account findByCreditCard(CreditCard creditCard);
}
