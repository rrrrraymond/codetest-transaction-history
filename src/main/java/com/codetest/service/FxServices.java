package com.codetest.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codetest.dto.CurrencyRateResponse;
import com.codetest.exception.GetCurrencyRateException;
import com.codetest.model.CurrencyRate;


@Service
public class FxServices {

	static Logger logger = LoggerFactory.getLogger(FxServices.class);
	
	private static final String URL = "http://api.exchangerate.host/latest?base=HKD";
	
	
	/**
	 * This should be calling external API
	 * for testing purpose, ride on the API provided by https://exchangerate.host/#/
	 * @return
	 * @throws GetCurrencyRateException 
	 */
	public Map<String, CurrencyRate> getCurrentFxMap() throws GetCurrencyRateException {
		try {
			final RestTemplate restTemplate = new RestTemplate();
			final ResponseEntity<CurrencyRateResponse> responseEntity = 
				    restTemplate.getForEntity(URL, CurrencyRateResponse.class); 
			final CurrencyRateResponse currencyRateResponse = responseEntity.getBody();
			final String base = currencyRateResponse.getBase();
			final String rateDate = currencyRateResponse.getDate();
			return currencyRateResponse.getRates().entrySet().parallelStream()
				.collect(Collectors.toMap(entry -> entry.getKey()+base, entry -> new CurrencyRate(entry.getKey()+base, BigDecimal.valueOf(1/entry.getValue()), rateDate)));
		} catch (Exception e) {
			logger.error("getCurrentFxMap Exception :: {}, {}", e.getMessage(), e);
			throw new GetCurrencyRateException(e);
		}
	}
}
