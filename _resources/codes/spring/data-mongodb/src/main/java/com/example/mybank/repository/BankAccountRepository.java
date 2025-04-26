package com.example.mybank.repository;

import com.example.mybank.domain.BankAccountDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BankAccountRepository extends MongoRepository<BankAccountDetails, String>, QuerydslPredicateExecutor<BankAccountDetails> {

    List<BankAccountDetails> findByFixedDepositsTenureAndFixedDepositsFdAmount(int tenure, int fdAmount);

    @Async
    CompletableFuture<List<BankAccountDetails>> findAllByBalanceGreaterThan(int balance);

    @Query("{'balance': {$lt: ?0}}")
    List<BankAccountDetails> findByCustomQuery(int balance);
}
