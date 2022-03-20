package sandbox;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.codetest.model.Transaction;

public class Tttest2 {

	static Logger logger = LoggerFactory.getLogger(Tttest2.class);

	class KafkaExample {
	    private final String topic;
	    private final Properties props;

	    public KafkaExample(String brokers, String username, String password) {
	        this.topic = username + "-default";

	        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
	        String jaasCfg = String.format(jaasTemplate, username, password);

	        String serializer = StringSerializer.class.getName();
	        String deserializer = StringDeserializer.class.getName();
	        String valueSerializer = JsonSerializer.class.getName();
	        String valueDeserializer = JsonDeserializer.class.getName();
	        props = new Properties();
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
	        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Transaction.class.getName());
	        props.put("security.protocol", "SASL_SSL");
	        props.put("sasl.mechanism", "SCRAM-SHA-256");
	        props.put("sasl.jaas.config", jaasCfg);
	    }

	    public void consume() {
//	        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	    	;
	        try (KafkaConsumer<String, Transaction> consumer = new KafkaConsumer<>(props)){
	//	        consumer.subscribe(Arrays.asList(topic));
	
		        TopicPartition topicPartition = new TopicPartition(topic, 0);
		        List<TopicPartition> partitions = Arrays.asList(topicPartition); 
		        
		        final long endOffset = consumer.endOffsets(partitions).get(topicPartition);
		        consumer.assign(partitions);
		        consumer.seekToBeginning(partitions);
		        
		        while (endOffset > 0 && consumer.position(topicPartition) < endOffset) {
		            ConsumerRecords<String, Transaction> records = consumer.poll(Duration.ofSeconds(1));
		            for (ConsumerRecord<String, Transaction> record : records) {
		                logger.info("{} [{}] offset={}, key={}, value=\"{}\"\n",
										  record.topic(), record.partition(),
										  record.offset(), record.key(), record.value());
					}
		            logger.debug("currentPos : {}, endOffset : {}", consumer.position(topicPartition), endOffset);
		        } 
		        
		        logger.info("DONE");
//		        Thread.sleep(5000);
            } catch (Exception v) {
                logger.error("error : {}", v);
//                throw v;
//            } finally {
//            	consumer.close();
            }
	        
	        
	        
	    }

	    public void produce() {
	        Thread one = new Thread() {
	            public void run() {
	            	Integer partition = 0;
	                try (Producer<String, Transaction> producer = new KafkaProducer<>(props)){
	                    int i = 0;
	                    
	                    
	                	final List<Transaction> list = new ArrayList<Transaction>();
	                	list.add(new Transaction(UUID.fromString("1b11d831-3e4d-4069-976b-26dd2eb3c40e"), "P-0123456789", BigDecimal.valueOf(-1.1D), "GBP", "GP93-0000-0000-0000-0000-0", "01-10-2020", "Online payment GBP"));
	                	list.add(new Transaction(UUID.fromString("1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec"), "P-0123456789", BigDecimal.valueOf(-2.2D), "CHF", "CH93-0000-0000-0000-0000-0", "01-10-2020", "Online payment CHF"));
	                	list.add(new Transaction(UUID.fromString("291342f2-03a5-4143-9396-1917be872b1e"), "P-0123456789", BigDecimal.valueOf(3.3D), "EUR", "EU93-0000-0000-0000-0000-0", "01-10-2020", "Stock Divident EUR"));
	                	list.add(new Transaction(UUID.fromString("8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca"), "P-0123456789", BigDecimal.valueOf(4.4D), "CHF", "CH93-0000-0000-0000-0000-0", "01-10-2020", "Interest CHF"));

	                	
	                	for (Transaction t : list) {
	                		ProducerRecord<String, Transaction> record = new ProducerRecord<>(topic, partition, t.getId().toString(), t);
//	                		producer.send(new ProducerRecord<>(topic, partition, t.getId(), d.toString()));
	                		producer.send(record);
	                		Thread.sleep(1000);
	                	}
	                	
//	                    while(true) {
//	                        Date d = new Date();
//	                        producer.send(new ProducerRecord<>(topic, partition, Integer.toString(i), d.toString()));
//	                        Thread.sleep(1000);
//	                        i++;
//	                    }
	                } catch (InterruptedException v) {
	                    logger.error("error : {}", v);
	                }
	            }
	        };
	        one.start();
	    }
	}

	

//    @Test
    public void main() {
		String brokers = "tricycle-01.srvs.cloudkafka.com:9094";
		String username = "kxuwr2vg";
		String password = "xKJo8D3NH0tEitg5nKyuouENQIziw58T";
		KafkaExample c = new KafkaExample(brokers, username, password);
//        c.produce();
        c.consume();
    }
}
