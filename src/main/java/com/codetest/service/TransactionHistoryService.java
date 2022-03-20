package com.codetest.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codetest.dto.TransactionHistoryResponse;
import com.codetest.exception.GetCurrencyRateException;
import com.codetest.exception.GetTransactionHistoryException;
import com.codetest.model.CurrencyRate;
import com.codetest.model.Transaction;

@Service
public class TransactionHistoryService {
	static Logger logger = LoggerFactory.getLogger(TransactionHistoryService.class);
	
	@Autowired
	KafkaTransactionHistoryService kafkaTransactionHistoryService;
	
	@Autowired
	FxServices fxServices;
	
	public TransactionHistoryResponse getTransactionList(String baseCurrency, String calendarMonth, String userId) throws GetCurrencyRateException, GetTransactionHistoryException {
		final Map<String, CurrencyRate> rateMap = fxServices.getCurrentFxMap();
		final List<Transaction> transactionList = kafkaTransactionHistoryService.getTransactionHistoryList(calendarMonth, userId);

		final TransactionHistoryResponse response = new TransactionHistoryResponse(BigDecimal.ZERO, BigDecimal.ZERO, transactionList);
		transactionList.stream().forEach(transaction -> {
			BigDecimal baseCurrencyAmount = transaction.getTransactionAmount().multiply(rateMap.get(transaction.getTransactionCurrency()+baseCurrency).getRate());
			if (transaction.getTransactionAmount().compareTo(BigDecimal.ZERO) >= 0) {
				response.setTotalDebitValue(response.getTotalDebitValue().add(baseCurrencyAmount));
			} else {
				response.setTotalCreditValue(response.getTotalCreditValue().add(baseCurrencyAmount));
			}
		});
		return response;
	}
	
}
