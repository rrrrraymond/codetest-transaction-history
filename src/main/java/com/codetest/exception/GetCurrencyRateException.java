package com.codetest.exception;

public class GetCurrencyRateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3378390522664462758L;

	public GetCurrencyRateException(Exception e) {
		super("Get Currency Rate Error", e);
	}
}
