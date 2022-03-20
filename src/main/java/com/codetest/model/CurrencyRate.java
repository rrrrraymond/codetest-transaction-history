package com.codetest.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CurrencyRate implements Serializable{

	private static final long serialVersionUID = 2420450897181981862L;
	
	private String currencyPair;
	private BigDecimal rate;
	private String rateDate;
	
	public CurrencyRate() {
		
	}
	
	public CurrencyRate(String currencyPair, BigDecimal rate, String rateDate) {
		super();
		this.currencyPair = currencyPair;
		this.rate = rate;
		this.rateDate = rateDate;
	}

	public String getCurrencyPair() {
		return currencyPair;
	}
	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getRateDate() {
		return rateDate;
	}
	public void setRateDate(String rateDate) {
		this.rateDate = rateDate;
	}
			
}
