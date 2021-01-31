package com.account.springrest.mongodb.controller;


import com.account.springrest.mongodb.model.Balance;
import com.account.springrest.mongodb.model.Transaction;
import com.account.springrest.mongodb.repository.BalanceRepository;
import com.account.springrest.mongodb.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

/**
 * Created by Jose Corominas on 01/27/21.
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    BalanceRepository balanceRepo;
    
    @Autowired
    TransactionRepository transactionRepo;

    private static final String WITHDRAW = "WITHDRAW";
    
    private static final String DEPOSIT = "DEPOSIT";
    
    private static final String YYYYMMDD = "yyyy-MM-dd";
    /*
     * 
     *  BALANCES
     *  
     */
    @GetMapping("/balance/latest/accountNumber/{accountNumber}")
    public Mono<ResponseEntity<Balance>> getLatestBalanceByAccountNumber(@PathVariable(value = "accountNumber") String accountNumber) {
    	List<Order> sortOrders = new ArrayList<Order>();
        Order sortOrder = new Order(Direction.DESC, "lastUpdateTimestamp");
        sortOrders.add(sortOrder);
    	return balanceRepo
    			.findByAccountNumber(accountNumber, Sort.by(sortOrders))
    			.next().map(savedBalance -> ResponseEntity.ok(savedBalance))
    			.defaultIfEmpty(ResponseEntity.notFound().build());
       
    }

    //Balances are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/balances", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Balance> streamAllBalances() {
        return balanceRepo.findAll();
    }
    
    @GetMapping("/balances/{id}")
    public Mono<ResponseEntity<Balance>> getBalanceById(@PathVariable(value = "id") String balanceId) {
        return balanceRepo.findById(balanceId)
                .map(savedBalance -> ResponseEntity.ok(savedBalance))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    
    @PostMapping("/balances/create")
    public Mono<ResponseEntity<Balance>> postBalance(@Valid @RequestBody Balance balance) {
        return balanceRepo.save(balance).map(newBalance -> new ResponseEntity<>(newBalance, HttpStatus.CREATED));
    }
    
    @DeleteMapping("/balances/{id}")
    public Mono<ResponseEntity<Void>> deleteBalance(@PathVariable(value = "id") String balanceId) {

        return balanceRepo.findById(balanceId)
                .flatMap(existingBalance ->
                        balanceRepo.delete(existingBalance)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/balances/delete")
   	public Mono<ResponseEntity<Void>> deleteAllBalances() {
   		System.out.println("Delete All Balances...");

   		return balanceRepo.deleteAll().then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
   	}
    
    @PutMapping("/balances/{id}")
    public Mono<ResponseEntity<Balance>> updateBalance(@PathVariable(value = "id") String balanceId,
                                                   @Valid @RequestBody Balance balance) {
        return balanceRepo.findById(balanceId)
                .flatMap(existingBalance -> {
                    existingBalance.setAccountNumber(balance.getAccountNumber());
                    existingBalance.setLastUpdateTimeStamp(balance.getLastUpdateTimestamp());
                    existingBalance.setBalance(balance.getBalance());
                    return balanceRepo.save(existingBalance);
                })
                .map(updateBalance -> new ResponseEntity<>(updateBalance, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    /*
     * 
     *  TRANSACTIONS
     *  
     */
   
    // GET /api/transactions/accountNumber/{accountNumber}/fromDate/{fromDate}/toDate/{toDate}
    // get all transactions by account number and greater than or equal to a from date and less than or equal to a to date with pagination
    // and in descending order by transactionTs
    @GetMapping("/transactions/accountNumber/{accountNumber}/fromDate/{fromDate}/toDate/{toDate}")
    public Flux<Transaction> findByAccountNumberAndDateRange(@PathVariable(value="accountNumber") String accountNumber,
    																   @PathVariable(value="fromDate") String fromDate,
    																   @PathVariable(value="toDate") String toDate,
    																   @RequestParam(name = "page", defaultValue = "0") int page,
    																   @RequestParam(name = "size", defaultValue = "3") int size) throws Exception
    																
    {
    	List<Order> sortOrders = descendingOrderByTransactionTs();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
        
        DateFormat format = new SimpleDateFormat(YYYYMMDD);
        Date _fromDate= format.parse(fromDate);
        //Add one day to the to date to include transactions on the to date
        Date _toDate = format.parse(toDate);
        Calendar c = Calendar.getInstance(); 
        c.setTime(_toDate); 
        c.add(Calendar.DATE, 1);
        Date _toDatePlusOneDay= c.getTime();
        
        return transactionRepo
        		.findByAccountNumberByTransactionTsRange(accountNumber, _fromDate, _toDatePlusOneDay, pageable);
    }
    
    // GET /api/transactions/accountNumber/{accountNumber}/type/{type}/fromDate/{fromDate}/toDate/{toDate}
    // get transactions by account number and by type and greater than or equal to a from date and less than or equal to a to date 
    // with pagination in descending order by transactionTs
    @GetMapping("/transactions/accountNumber/{accountNumber}/type/{type}/fromDate/{fromDate}/toDate/{toDate}")
    public Flux<Transaction> findByAccountNumberByTypeByTransactionTsRange(@PathVariable(value="accountNumber") String accountNumber,
    																   @PathVariable(value="type") String type,
    																   @PathVariable(value="fromDate") String fromDate,
    																   @PathVariable(value="toDate") String toDate,
    																   @RequestParam(name = "page", defaultValue = "0") int page,
    																   @RequestParam(name = "size", defaultValue = "3") int size) throws Exception
    {
    	if(type==null || !WITHDRAW.equals(type) && !DEPOSIT.equals(type)){
    		throw new Exception("Invalid Transaction Type");
    	}
    	
    	List<Order> sortOrders = descendingOrderByTransactionTs();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
        
        
        DateFormat format = new SimpleDateFormat(YYYYMMDD);
        Date _fromDate= format.parse(fromDate);
        Date _toDate = format.parse(toDate);
        Calendar c = Calendar.getInstance(); 
        c.setTime(_toDate); 
        c.add(Calendar.DATE, 1);
        Date _toDatePlusOneDay= c.getTime();
        
        return transactionRepo.findByAccountNumberByTypeByTransactionTsRange(accountNumber, type, _fromDate, _toDatePlusOneDay, pageable);
    }
    
    //Transactions are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/transactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamAllTransactions() {
        return transactionRepo.findAll();
    }
    
    @GetMapping("/transactions/{id}")
    public Mono<ResponseEntity<Transaction>> getTransactionById(@PathVariable(value = "id") String transactionId) {
        return transactionRepo.findById(transactionId)
                .map(savedTransaction -> ResponseEntity.ok(savedTransaction))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/transactions/create")
    public Mono<ResponseEntity<Transaction>> postTransaction(@Valid @RequestBody Transaction transaction) throws Exception {
    	if(transaction.getType()==null || transaction.getType() != WITHDRAW && transaction.getType() != DEPOSIT){
    		throw new Exception("Invalid Transaction Type");
    	}
        return transactionRepo.save(transaction).map(newTransaction -> new ResponseEntity<>(newTransaction, HttpStatus.CREATED));
    }
    
    @DeleteMapping("/transactions/{id}")
    public Mono<ResponseEntity<Void>> deleteTransaction(@PathVariable(value = "id") String transactionId) {

        return transactionRepo.findById(transactionId)
                .flatMap(existingTransaction ->
                        transactionRepo.delete(existingTransaction)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/transactions/delete")
   	public Mono<ResponseEntity<Void>> deleteAllTransactions() {
   		System.out.println("Delete All Transactions...");

   		return transactionRepo.deleteAll().then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
   	}
    
    @PutMapping("/transactions/{id}")
    public Mono<ResponseEntity<Transaction>> updateTransaction(@PathVariable(value = "id") String transactionId,
                                                   @Valid @RequestBody Transaction transaction) throws Exception {
    	if(transaction.getType()==null || !WITHDRAW.equals(transaction.getType()) && !DEPOSIT.equals(transaction.getType())){
    		throw new Exception("Invalid Transaction Type");
    	}
        return transactionRepo.findById(transactionId)
                .flatMap(existingTransaction -> {
                    existingTransaction.setAccountNumber(transaction.getAccountNumber());
                    existingTransaction.setTransactionTs(transaction.getTransactionTs());
                    existingTransaction.setType(transaction.getType());
                    existingTransaction.setAmount(transaction.getAmount());
                    return transactionRepo.save(existingTransaction);
                })
                .map(updateTransaction -> new ResponseEntity<>(updateTransaction, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    private List<Order> descendingOrderByTransactionTs() {
		List<Order> sortOrders = new ArrayList<Order>();
        Order sortOrder = new Order(Direction.DESC, "transactionTs");
        sortOrders.add(sortOrder);
		return sortOrders;
	}

}
