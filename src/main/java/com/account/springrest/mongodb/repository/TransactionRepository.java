package com.account.springrest.mongodb.repository;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.account.springrest.mongodb.model.Transaction;

import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String>{
	@Query("{accountNumber: ?0, transactionTs: {$gte: ?1, $lte: ?2}}")
	Flux<Transaction> findByAccountNumberByTransactionTsRange(String accountNumber, Date fromDate, Date toDate, Pageable pageable);

	@Query("{accountNumber: ?0, type: ?1, transactionTs: {$gte: ?2, $lte: ?3}}")
	Flux<Transaction> findByAccountNumberByTypeByTransactionTsRange(String accountNumber, String type, Date fromDate, Date toDate, Pageable pageable);
	
}
