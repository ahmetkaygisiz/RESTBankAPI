package com.restbank.repository;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    Account findByAccountNumber(String accountNumber);

    @Transactional
    List<Account> findAllByUser(User user);

    @Transactional
    Account findByCreditCard(CreditCard creditCard);

    @Transactional
    void deleteByAccountNumber(String accountNumber);
}
