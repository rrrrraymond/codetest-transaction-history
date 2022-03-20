package com.codetest.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TopicAuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codetest.exception.GetTransactionHistoryException;
import com.codetest.model.Transaction;

//kafka-related
@Service
public class KafkaTransactionHistoryService {
	static Logger logger = LoggerFactory.getLogger(KafkaTransactionHistoryService.class);
	
    @Value("${kafka.cloudkarafka.username}")
    String username;
    
    @Autowired
    KafkaConsumer<String, Transaction> kafkaTransactionHistoryConsumer;
    
    @Autowired
    Producer<String, Transaction> kafkaTransactionHistoryProducer;
    
    private static Object getLock = new Object();
    private static Object putLock = new Object();
    
	/**
	 * Using the bean created in KafkaTransactionHistoryConfiguration
	 * Connect to the kafka and get the transaction list. The transaction list are group by calendar month, separated by topic.
	 * @return
	 * @throws GetTransactionHistoryException 
	 */
	public List<Transaction> getTransactionHistoryList(String calendarMonth, String userId) throws GetTransactionHistoryException {
    	final List<Transaction> list = new ArrayList<Transaction>();

    	synchronized(getLock) {
    		try {
        		String topic = username + "-" + calendarMonth;
    	
    	        TopicPartition topicPartition = new TopicPartition(topic, 0);
    	        List<TopicPartition> partitions = Arrays.asList(topicPartition); 
    	        
    	        final long endOffset = kafkaTransactionHistoryConsumer.endOffsets(partitions).get(topicPartition);
    	        kafkaTransactionHistoryConsumer.assign(partitions);
    	        kafkaTransactionHistoryConsumer.seekToBeginning(partitions);
    	        
    	        while (endOffset > 0 && kafkaTransactionHistoryConsumer.position(topicPartition) < endOffset) {
    	            ConsumerRecords<String, Transaction> records = kafkaTransactionHistoryConsumer.poll(Duration.ofSeconds(1));
    	            for (ConsumerRecord<String, Transaction> record : records) {
    	            	if (userId.equals(record.value().getUserId())){
    	            		list.add(record.value());
    	            	}
    				}
    	            logger.info("currentPos : {}, endOffset : {}", kafkaTransactionHistoryConsumer.position(topicPartition), endOffset);
    	        } 
    	        
    	        logger.info("DONE");
    	        return list;
    		} catch (TopicAuthorizationException e) {
    			logger.error("TopicAuthorizationException : {}", e);
        	    throw new GetTransactionHistoryException("Calendar Month not found", e);
            } catch (Exception v) {
                logger.error("error : {}", v);
        	    throw new GetTransactionHistoryException(v);
            }
    	}
    }
	
	
	/**
	 * Using the bean created in KafkaTransactionHistoryConfiguration
	 * Connect to the kafka and put the transaction list. The transaction list are group by calendar month, separated by topic.
	 * @param list
	 * @param calendarMonth
	 */
	public void putTransactionHistoryList(List<Transaction> list, String calendarMonth) {
		Integer partition = 0;
		
		String topic = username + "-" + calendarMonth;
		synchronized(putLock) {
	        try {
	        	for (Transaction t : list) {
	        		ProducerRecord<String, Transaction> record = new ProducerRecord<>(topic, partition, t.getId().toString(), t);
	        		kafkaTransactionHistoryProducer.send(record);
	        		Thread.sleep(1000);
	        	}
	        	
	        } catch (InterruptedException v) {
	            logger.error("error : {}", v);
	        }
		}
	}
}
