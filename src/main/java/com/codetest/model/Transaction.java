package com.codetest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 14218727992715904L;
	
	private UUID id;
	private String userId;
	private BigDecimal transactionAmount;
	private String transactionCurrency;
	private String accountIBAN;
	private String valueDate;
	private String description;
	
	public Transaction(UUID id, String userId, BigDecimal transactionAmount, String transactionCurrency,
			String accountIBAN, String valueDate, String description) {
		super();
		this.id = id;
		this.userId = userId;
		this.transactionAmount = transactionAmount;
		this.transactionCurrency = transactionCurrency;
		this.accountIBAN = accountIBAN;
		this.valueDate = valueDate;
		this.description = description;
	}

	public Transaction() {
		
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionCurrency() {
		return transactionCurrency;
	}
	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}
	public String getAccountIBAN() {
		return accountIBAN;
	}
	public void setAccountIBAN(String accountIBAN) {
		this.accountIBAN = accountIBAN;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", userId=" + userId + ", transactionAmount=" + transactionAmount
				+ ", transactionCurrency=" + transactionCurrency + ", accountIBAN=" + accountIBAN + ", valueDate="
				+ valueDate + ", description=" + description + "]";
	}
	
}
