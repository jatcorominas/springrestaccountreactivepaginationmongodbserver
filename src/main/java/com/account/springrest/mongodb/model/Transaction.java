package com.account.springrest.mongodb.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transaction")
public class Transaction {
	@Id
	private String id;
	private String accountNumber;
	private Date   transactionTs;
	private String type;
	private BigDecimal amount;
	
	public Transaction(){
	}
	
	public Transaction(String accountNumber, Date transactionTs, String type, BigDecimal amount){
		this.accountNumber=accountNumber;
		this.transactionTs=transactionTs;
		this.type=type;
		this.amount=amount;
	}
	
	public String getId(){
		return this.id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getTransactionTs() {
		return transactionTs;
	}

	public void setTransactionTs(Date transactionTs) {
		this.transactionTs = transactionTs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", accountNumber=" + accountNumber + ", transactionTs=" + transactionTs
				+ ", type=" + type + ", amount=" + amount + "]";
	}
	
	
}
