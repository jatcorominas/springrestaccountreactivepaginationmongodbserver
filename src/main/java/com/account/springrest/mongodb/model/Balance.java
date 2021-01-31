package com.account.springrest.mongodb.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "balance")
public class Balance {
	@Id
	private String id;
	private String accountNumber;
	private Date lastUpdateTimestamp;
	private BigDecimal balance;
	

	public Balance() {
	}

	public Balance(String accountNumber, Date lastUpdateTimeStamp, BigDecimal balance) {
		this.accountNumber = accountNumber;
		this.lastUpdateTimestamp = lastUpdateTimeStamp;
		this.balance = balance;
	}

	public String getId(){
		return this.id;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber){
		this.accountNumber=accountNumber;
	}

	public void setLastUpdateTimeStamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public Date getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	@Override
	public String toString() {
		return "Balance [id=" + id + "accountNumber=" + accountNumber + ", lastUpdateTimestamp=" + lastUpdateTimestamp + ", balance=" + balance + "]";
	}
}
