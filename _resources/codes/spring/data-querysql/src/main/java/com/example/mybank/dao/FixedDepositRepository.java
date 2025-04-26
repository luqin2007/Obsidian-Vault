package com.example.mybank.dao;

import com.example.mybank.domain.FixedDepositDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FixedDepositRepository extends JpaRepository<FixedDepositDetails, Integer>, QuerydslPredicateExecutor<FixedDepositDetails> {
}
