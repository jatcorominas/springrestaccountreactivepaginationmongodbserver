package com.account.springrest.mongodb.repository;

import com.account.springrest.mongodb.model.Balance;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface BalanceRepository extends ReactiveMongoRepository<Balance, String>{

	Flux<Balance> findByAccountNumber(String accountNumber, Sort sort);
}
