package com.codetest.configuration;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codetest.model.Transaction;
import com.codetest.service.KafkaService;

// kafka-related
@Configuration
public class KafkaTransactionHistoryConfiguration {

    @Autowired
    KafkaService<Transaction> kafkaService;
    
    @Bean
    public KafkaConsumer<String, Transaction> kafkaTransactionHistoryConsumer() {
    	return kafkaService.getKafkaConsumer(Transaction.class);
    }
    
    @Bean
    public Producer<String, Transaction> kafkaTransactionHistoryProducer() {
    	return kafkaService.getKafkaProducer();
    }
}
