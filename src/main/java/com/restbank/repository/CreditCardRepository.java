package com.restbank.repository;

import com.restbank.domain.CreditCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long > {
    Page<CreditCard> findAll(Pageable page);
}
