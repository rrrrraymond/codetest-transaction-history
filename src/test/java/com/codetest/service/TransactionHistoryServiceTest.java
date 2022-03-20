package com.codetest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codetest.dto.TransactionHistoryResponse;
import com.codetest.exception.GetCurrencyRateException;
import com.codetest.exception.GetTransactionHistoryException;
import com.codetest.model.CurrencyRate;
import com.codetest.model.Transaction;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionHistoryServiceTest {

	static Logger logger = LoggerFactory.getLogger(TransactionHistoryServiceTest.class);
	
	@Mock
	KafkaTransactionHistoryService kafkaTransactionHistoryService;
	
	@Mock
	FxServices fxServices;
	
	@InjectMocks
	public TransactionHistoryService transactionHistoryService;
	
	
	@Test
	public void test1_getTransactionList_success() throws GetCurrencyRateException, GetTransactionHistoryException {
		String calendarMonth = "202010";
		String userId = "P-3323456789";
		
    	final List<Transaction> mocklist = new ArrayList<Transaction>();
    	mocklist.add(new Transaction(UUID.fromString("1b11d831-3e4d-4069-976b-26dd2eb3c40e"), userId, BigDecimal.valueOf(-1.1D), "GBP", "GP93-0000-0000-0000-0000-0", "01-10-2020", "Online payment GBP"));
    	mocklist.add(new Transaction(UUID.fromString("1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec"), userId, BigDecimal.valueOf(-2.2D), "CHF", "CH93-0000-0000-0000-0000-0", "01-10-2020", "Online payment CHF"));
    	mocklist.add(new Transaction(UUID.fromString("291342f2-03a5-4143-9396-1917be872b1e"), userId, BigDecimal.valueOf(3.3D), "EUR", "EU93-0000-0000-0000-0000-0", "01-10-2020", "Stock Divident EUR"));
    	mocklist.add(new Transaction(UUID.fromString("8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca"), userId, BigDecimal.valueOf(4.4D), "CHF", "CH93-0000-0000-0000-0000-0", "01-10-2020", "Interest CHF"));

    	final Map<String, CurrencyRate> currentFxMap = new HashMap<String, CurrencyRate>();
    	currentFxMap.put("GBPHKD", new CurrencyRate("GBPHKD", BigDecimal.valueOf(10.26D), "2022-03-22") );
    	currentFxMap.put("EURHKD", new CurrencyRate("EURHKD", BigDecimal.valueOf(8.66D), "2022-03-22") );
    	currentFxMap.put("CHFHKD", new CurrencyRate("CHFHKD", BigDecimal.valueOf(10.39D), "2022-03-22") );
    	
		doReturn(currentFxMap).when(fxServices).getCurrentFxMap();
		doReturn(mocklist).when(kafkaTransactionHistoryService).getTransactionHistoryList(calendarMonth, userId);
		
		TransactionHistoryResponse response = transactionHistoryService.getTransactionList("HKD", calendarMonth, userId);
		logger.info("response : {}, {}, {}", response.getTotalDebitValue(), response.getTotalCreditValue(), response.getTransactionList());
		assertEquals(BigDecimal.valueOf(74.294D), response.getTotalDebitValue());
		assertEquals(BigDecimal.valueOf(-34.144D), response.getTotalCreditValue());
		assertEquals(4, response.getTransactionList().size());
	}
}
