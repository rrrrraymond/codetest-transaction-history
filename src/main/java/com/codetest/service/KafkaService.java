package com.codetest.service;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

//kafka-related
@Service
public class KafkaService<T> {
	
	static Logger logger = LoggerFactory.getLogger(KafkaService.class);
	
    private final Properties props = new Properties();
    
    @Value("${kafka.cloudkarafka.brokers}")
    String brokers;
    @Value("${kafka.cloudkarafka.username}")
    String username;
    @Value( "${kafka.cloudkarafka.password}" )
    String password;
    
    @PostConstruct
    public void init() {
    	
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();
        String valueSerializer = JsonSerializer.class.getName();
        String valueDeserializer = JsonDeserializer.class.getName();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", username + "-consumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", valueDeserializer);
        props.put("key.serializer", serializer);
        props.put("value.serializer", valueSerializer);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", jaasCfg);
    }
    
    public void test() {
    	logger.info("broker : {}, username : {}", brokers, username);
    }
    
    public KafkaConsumer<String, T> getKafkaConsumer(Class<T> clazz){
    	props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, clazz.getName());
    	return new KafkaConsumer<>(props);
    }
    
    public KafkaProducer<String, T> getKafkaProducer(){
    	return new KafkaProducer<>(props);
    }
}
