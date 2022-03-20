package com.codetest.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.codetest.TransactionHistoryApplication;
import com.codetest.exception.GetTransactionHistoryException;
import com.codetest.model.Transaction;

/**
 * 
 * This class is for connectivity test only. The Class itself calls downstream API, and no unit test needed.
 * @author Raymond
 *
 */
//@SpringBootTest
@ContextConfiguration(classes = TransactionHistoryApplication.class)
public class KafkaTransactionHistoryServiceTest {
	
	static Logger logger = LoggerFactory.getLogger(KafkaTransactionHistoryServiceTest.class);

	@Autowired
	KafkaTransactionHistoryService kafkaTransactionHistoryService;
	
//	@Test
	public void consummerConnectivityTest() throws GetTransactionHistoryException {
		for (Transaction t: kafkaTransactionHistoryService.getTransactionHistoryList("202010", "P-0123456789")) {
    		logger.info("ttttt : {}", t);
    	};
	}
	
//	@Test
	public void producerConnectivityTest() {
		
//		String userId = "P-0123456789";
		String userId = "P-3323456789";
		
    	final List<Transaction> list = new ArrayList<Transaction>();
    	list.add(new Transaction(UUID.fromString("1b11d831-3e4d-4069-976b-26dd2eb3c40e"), userId, BigDecimal.valueOf(-2.2D), "GBP", "GP93-1000-0000-0000-0000-0", "01-11-2020", "Online payment GBP"));
    	list.add(new Transaction(UUID.fromString("1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec"), userId, BigDecimal.valueOf(-4.4D), "CHF", "CH93-1000-0000-0000-0000-0", "01-11-2020", "Online payment CHF"));
    	list.add(new Transaction(UUID.fromString("291342f2-03a5-4143-9396-1917be872b1e"), userId, BigDecimal.valueOf(6.6D), "EUR", "EU93-1000-0000-0000-0000-0", "01-11-2020", "Stock Divident EUR"));
    	list.add(new Transaction(UUID.fromString("8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca"), userId, BigDecimal.valueOf(8.8D), "CHF", "CH93-1000-0000-0000-0000-0", "01-11-2020", "Interest CHF"));

    	try {
	    	kafkaTransactionHistoryService.putTransactionHistoryList(list, "202011");
	    	Thread.sleep(5000L);
    	} catch (Exception e) {
    		logger.error("Exception : {}, {}", e.getMessage(), e);
    	}
	}
}
