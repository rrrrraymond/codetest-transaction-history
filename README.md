# Read Me First
This is about the project to build a Bank Transaction History system.

# What is used in this project
* Spring-boot
* Spring Kafka - Connected to a developer-duck account at cloudkarafka
* Spring Security
* Swagger - springdoc-openapi-ui

# How to run this project
1. Check out this project
2. Run mvn clean install
3. java -jar transaction-history-0.0.1-SNAPSHOT.jar
4. The swagger-ui page is http://localhost:18081/swagger-ui.html


# Technical Design
## API Endpoints
There are 2 API endpoints for this Project
1. POST to get the JWT Token 
http://localhost:18081/authenticate

username:password  
P-0123456789:abc123  
P-3323456789:123abc  

2. GET Transaction History
http://localhost:18081/transaction-history/calendarMonth/202010

## Module 1: FxService
Leverage on online resource from https://exchangerate.host/#/
  
Sample API endpoint :   http://api.exchangerate.host/latest?base=HKD  

Use RestTemplate to de-serialise the response and map to POJO Map.
** the map can be cached to improved performance

## Module 2: Kafka Transaction History Service
Created a generic type of KafkaService for easy creation of producer and consumer.  
Created a testing kafka instance at cloudkarafka for POC purpose.  
Created a producer to push records to the testing kafka instance. (only available in testing class: KafkaTransactionHistoryServiceTest.java)  
Created a consumer to poll all the records from the testing kafka instance. (available in testing class: KafkaTransactionHistoryServiceTest.java, and the code logic)  
For POC only, the getTransactionList Function is synchronized. 

** the performance can be improved by allowing pool of consumers to read through the message in the topic partition.    



## Module 3: Transaction History Service
Stream loop through the transaction list from kafka, and convert the amount to base corrency (currently only HKD supported), and calculate the Total Debit Value and Total Credit Value

## Module 4: Spring Security 
Enable an API end-point to generate the bearer token.  

Enforce all other end-point to require authorized.  
** security can be further improved by authorities group.

## Module 5: Swagger
Include Swagger Module to the project.  
Enable the "Authorize" section on the swagger-ui.  
White-list the items for Swagger-ui end-point at Spring security.



# Data Preparation
id,userId,transactionAmount,transactionCurrency,accountIBAN,valueDate,description  
1b11d831-3e4d-4069-976b-26dd2eb3c40e,P-0123456789,-1.1,GBP,GP93-0000-0000-0000-0000-0,01-10-2020,Online payment GBP  
1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec,P-0123456789,-2.2,CHF,CH93-0000-0000-0000-0000-0,01-10-2020,Online payment CHF  
291342f2-03a5-4143-9396-1917be872b1e,P-0123456789,3.3,EUR,EU93-0000-0000-0000-0000-0,01-10-2020,Stock Divident EUR  
8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca,P-0123456789,4.4,CHF,CH93-0000-0000-0000-0000-0,01-10-2020,Interest CHF  
1b11d831-3e4d-4069-976b-26dd2eb3c40e,P-0123456789,-1.1,GBP,GP93-0000-0000-0000-0000-0,01-10-2020,Online payment GBP  
1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec,P-0123456789,-2.2,CHF,CH93-0000-0000-0000-0000-0,01-10-2020,Online payment CHF  
291342f2-03a5-4143-9396-1917be872b1e,P-0123456789,3.3,EUR,EU93-0000-0000-0000-0000-0,01-10-2020,Stock Divident EUR  
8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca,P-0123456789,4.4,CHF,CH93-0000-0000-0000-0000-0,01-10-2020,Interest CHF  
1b11d831-3e4d-4069-976b-26dd2eb3c40e,P-3323456789,-2.2,GBP,GP93-1000-0000-0000-0000-0,01-11-2020,Online payment GBP  
1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec,P-3323456789,-4.4,CHF,CH93-1000-0000-0000-0000-0,01-11-2020,Online payment CHF  
291342f2-03a5-4143-9396-1917be872b1e,P-3323456789,6.6,EUR,EU93-1000-0000-0000-0000-0,01-11-2020,Stock Divident EUR  
8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca,P-3323456789,8.8,CHF,CH93-1000-0000-0000-0000-0,01-11-2020,Interest CHF  
b11d831-3e4d-4069-976b-26dd2eb3c40e,P-3323456789,-1.1,GBP,GP93-0000-0000-0000-0000-0,01-10-2020,Online payment GBP  
1bfe68a7-52d0-4bb4-8d44-8a6d06f29bec,P-3323456789,-2.2,CHF,CH93-0000-0000-0000-0000-0,01-10-2020,Online payment CHF  
291342f2-03a5-4143-9396-1917be872b1e,P-3323456789,3.3,EUR,EU93-0000-0000-0000-0000-0,01-10-2020,Stock Divident EUR  
8e2754be-1df5-4fd1-9afa-23ba2dcaa6ca,P-3323456789,4.4,CHF,CH93-0000-0000-0000-0000-0,01-10-2020,Interest CHF  

# Assumption and Limitation
1. The data, produced in the kafka, is assumed to be correct (only 1 message per record, and hopefully in chronological order).
2. Better data structure design, can separate different customer into different partition of each calendar month (topic), so that not all the messages in the topic are scanned.
3. The kafka limitation (4000 topics and 4000 partitions is considered to be performance threshold) seems do not fit the requirement of 100,000s of transactions for each of the 1000s of consumers per calendar month. The suggestion is to put the calendar month in the topic. This, still, means read over 100,000s x 1000s message / 4000 partitions (per call).  
4. Consider to use noSQL DB like mongoDB and AWS DynamoDB, which are good at storing key-value pairs and blob. The key will be userId-calendarMonth, and the value will be JsonArray of ordered transactions. 
