package com.codetest.exception;

public class GetTransactionHistoryException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -894197283952384544L;

	public GetTransactionHistoryException(Exception e) {
		super("Get Transaction History Error", e);
	}
	
	public GetTransactionHistoryException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}
}
