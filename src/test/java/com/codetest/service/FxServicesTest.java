package com.codetest.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.codetest.exception.GetCurrencyRateException;
import com.codetest.model.CurrencyRate;

/**
 * 
 * This class is for connectivity test only. The Class itself calls downstream API, and no unit test needed.
 * @author Raymond
 *
 */
//@SpringBootTest
public class FxServicesTest {
	
	static Logger logger = LoggerFactory.getLogger(FxServicesTest.class);
	
	@Autowired
	public FxServices fxServices;
	
//	@Test
	public void test1() throws GetCurrencyRateException {
		Map<String, CurrencyRate> rateMap= fxServices.getCurrentFxMap();
		assertTrue(rateMap.containsKey("USDHKD"));
		assertTrue(rateMap.containsKey("EURHKD"));
		assertTrue(rateMap.containsKey("GBPHKD"));
		assertTrue(rateMap.containsKey("CHFHKD"));
	}
}
