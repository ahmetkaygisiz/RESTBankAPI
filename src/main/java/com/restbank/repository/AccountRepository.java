package com.restbank.repository;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    Account findAccountByAccountNumber(String accountNumber);

    @Transactional
    Account findByUser(User user);

    @Transactional
    Account findByCreditCard(CreditCard creditCard);

    @Transactional
    void deleteByAccountNumber(String accountNumber);
}
