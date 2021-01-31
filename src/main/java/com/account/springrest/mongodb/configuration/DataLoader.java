package com.account.springrest.mongodb.configuration;

import com.account.springrest.mongodb.repository.TransactionRepository;

import reactor.core.publisher.Flux;

import com.account.springrest.mongodb.model.Balance;
import com.account.springrest.mongodb.model.Transaction;
import com.account.springrest.mongodb.repository.BalanceRepository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

	private final BalanceRepository balanceRepository;
	
	private final TransactionRepository transactionRepo;
	
	private static final String YYYYMMDD = "yyyy-MM-dd";
	
	DataLoader(final BalanceRepository balanceRepository, final TransactionRepository transactionRepo){
		this.balanceRepository=balanceRepository;
		this.transactionRepo=transactionRepo;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(YYYYMMDD);
		
		// Populate the Balance repository
		// If the Balance repository is not empty, delete everything
		// then load the data
		if(balanceRepository.count().block() != 0L){
			balanceRepository.deleteAll();
		}
		
		List<Balance> balanceData = new ArrayList<Balance>();
		
		Balance balance = new Balance("123456",dateFormat.parse("2021-01-01T01:02:03.8Z"),new BigDecimal(28.1));
		balanceData.add(balance);
		
		balance = new Balance("123456",dateFormat.parse("2021-02-02T01:02:03.8Z"),new BigDecimal(70.3));
		balanceData.add(balance);
		
		balance = new Balance("123456",dateFormat.parse("2021-03-03T05:30:03.8Z"),new BigDecimal(100.0));
		balanceData.add(balance);
		
		balance = new Balance("7890",dateFormat.parse("2021-04-01T05:30:03.8Z"),new BigDecimal(87.1));
		balanceData.add(balance);
		
		balance = new Balance("7890",dateFormat.parse("2021-05-02T06:30:03.8Z"),new BigDecimal(76.2));
		balanceData.add(balance);
		
		balance = new Balance("7890",dateFormat.parse("2021-06-03T07:30:03.8Z"),new BigDecimal(500.99));
		balanceData.add(balance);
		
		Flux<Balance> savedBalances = this.balanceRepository.saveAll(balanceData);
		
		System.out.println("The Balance repository now has " + balanceRepository.count().block() + " tuples.");
		
		// Populate the Transaction repository
	    // If the Transaction repository is not empty, delete everything
	    // then load the data
		if( transactionRepo.count().block()!=0L){
			transactionRepo.deleteAll();
		}
		
		List<Transaction> transactionData = new ArrayList<Transaction>();
		
		Transaction trans=new Transaction("123456"
				, dateFormat.parse("2021-06-03T07:30:03.8Z")
				, "DEPOSIT"
				, new BigDecimal(75.2) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-04T09:45:03.8Z")
				, "DEPOSIT"
				, new BigDecimal(65.2) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-04T09:45:03.8Z")
				, "DEPOSIT"
				, new BigDecimal(65.2) );
		transactionData.add(trans);
		
		trans=new Transaction("7890"
				, dateFormat.parse("2021-01-06T06:10:03.8Z")
				, "DEPOSIT"
				, new BigDecimal(39.1) );
		transactionData.add(trans);
		
		trans=new Transaction("7890"
				, dateFormat.parse("2021-01-07T09:45:03.8Z")
				, "DEPOSIT"
				, new BigDecimal(59.2) );
		transactionData.add(trans);
		
		trans=new Transaction("7890"
				, dateFormat.parse("2021-01-08T12:55:03.8Z")
				, "DEPOSIT"
				, new BigDecimal(42.7) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-04T10:20:05.8Z")
				, "WITHDRAW"
				, new BigDecimal(13.2) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-05T11:20:05.8Z")
				, "WITHDRAW"
				, new BigDecimal(84.6) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-06T15:59:10.8Z")
				, "WITHDRAW"
				, new BigDecimal(100.9) );
		transactionData.add(trans);
		
		trans=new Transaction("7890"
				, dateFormat.parse("2021-01-07T16:20:05.8Z")
				, "WITHDRAW"
				, new BigDecimal(13.2) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-08T17:20:05.8Z")
				, "WITHDRAW"
				, new BigDecimal(84.6) );
		transactionData.add(trans);
		
		trans=new Transaction("123456"
				, dateFormat.parse("2021-01-09T18:59:10.8Z")
				, "WITHDRAW"
				, new BigDecimal(100.9) );
		transactionData.add(trans);
		
		trans=new Transaction("7890"
				, dateFormat.parse("2021-01-10T16:20:05.8Z")
				, "WITHDRAW"
				, new BigDecimal(55.7) );
		transactionData.add(trans);
		
		trans=new Transaction("7890"
				, dateFormat.parse("2021-01-28T16:20:05.8Z")
				, "WITHDRAW"
				, new BigDecimal(849.3) );
		transactionData.add(trans);
		
		Flux<Transaction> savedTransactions = transactionRepo.saveAll(transactionData);
		System.out.println("The Transaction repository now has " + transactionRepo.count().block() + " tuples.");
		
	}

}
