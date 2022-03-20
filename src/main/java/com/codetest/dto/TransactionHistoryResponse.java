package com.codetest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.codetest.model.Transaction;

public class TransactionHistoryResponse implements Serializable{
	private static final long serialVersionUID = -3427213388462398308L;

	private BigDecimal totalDebitValue;
	private BigDecimal totalCreditValue;
	private List<Transaction> transactionList;
	
	
	public TransactionHistoryResponse(BigDecimal totalDebitValue, BigDecimal totalCreditValue,
			List<Transaction> transactionList) {
		super();
		this.totalDebitValue = totalDebitValue;
		this.totalCreditValue = totalCreditValue;
		this.transactionList = transactionList;
	}

	public TransactionHistoryResponse() {
		
	}
	
	public BigDecimal getTotalDebitValue() {
		return totalDebitValue;
	}
	public void setTotalDebitValue(BigDecimal totalDebitValue) {
		this.totalDebitValue = totalDebitValue;
	}
	public BigDecimal getTotalCreditValue() {
		return totalCreditValue;
	}
	public void setTotalCreditValue(BigDecimal totalCreditValue) {
		this.totalCreditValue = totalCreditValue;
	}
	public List<Transaction> getTransactionList() {
		return transactionList;
	}
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
	
	
}
