package com.example.mybank.dao;

import com.example.mybank.domain.BankAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BankAccountRepository extends JpaRepository<BankAccountDetails, Integer>, QuerydslPredicateExecutor<BankAccountDetails> {
}
