package com.codetest.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codetest.dto.TransactionHistoryResponse;
import com.codetest.exception.GetCurrencyRateException;
import com.codetest.exception.GetTransactionHistoryException;
import com.codetest.service.TransactionHistoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class TransactionHistoryController {
	
	Logger logger = LoggerFactory.getLogger(TransactionHistoryController.class);
	
	private static final String BASE_CURRENCY = "HKD";

	@Autowired
	public TransactionHistoryService transactionHistoryService;
	
	/**
	 *  API end-point for querying Transaction History
	 * @param categoryId
	 * @return
	 * @throws GetCurrencyRateException 
	 * @throws GetTransactionHistoryException 
	 */
	@RequestMapping(value = "/transaction-history/calendarMonth/{calendarMonth}", method = RequestMethod.GET, produces = "application/json")
	public TransactionHistoryResponse getTransactionHistoryByMonth(@PathVariable String calendarMonth, Principal principal) throws GetCurrencyRateException, GetTransactionHistoryException {
//		try {
			return transactionHistoryService.getTransactionList(BASE_CURRENCY, calendarMonth, principal.getName());
//		} catch (GetCurrencyRateException e) {
//			return null;
//		}
	}
}
