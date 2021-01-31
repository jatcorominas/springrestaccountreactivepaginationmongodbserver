README.MD

This is a Representational State Transfer (REST) Accounts CRUD (Create-Retrieve-Update-Delete) application that is a Spring-boot webflux reactive, asynchronous
microservice that manipulates balance and transaction data on the Mongo DB No-Sql database.

Author: Jose Alfonso Corominas
email:  jatcorominas@gmail.com
cell:   +1-647-618-2080

I. RATIONALE

1. Why choose MongoDB NoSql?

   It is widely used for big data reporting because of its ability to efficiently process large volumes of unstructured data. 
   It is easier to increase capacity by adding more servers without taking down existing servers so there will be minimal downtime. 
   This is called "scaling out" or horizontal scaling. It has more sophisticated querying features that makes it well suited for big data reporting.

2. What do you do in case querying transactions by account number and type and date range returns large amount of data?

   The problem with queries that return millions of transactions data is that the system can be overwhelmed. 

   The solution to this is to

   (a) Use the springboot MongoDB Webflux reactive asynchronous non-blocking api to spawn query requests in secondary threads to enable the main 
       thread to do other things.
   (b) Implement pagination so that the end user will see results in a timely manner without waiting for all the data to be retrieved.
   (c) This is realized by extending the springboot ReactiveMongoRepository interface because it leverages the MongoDB Webflux reactive asynchronous
       non-blocking api and supports pagination directly. The Transactions repository interface extend the ReactiveMongoRepository to provide
       functionality to query transactions by account number, type and date range with pagination. The Balances repository extends the ReactiveMongoRepository 
       to provide behviour to find the latest balance by account number.

I. BALANCE REST API

GET

(a) latest balance by account number 

    GET /api/balance/latest/accountNumber/{accountNumber}

(b) stream all balances no pagination

    GET /api/stream/balances

(c) get balance by id

    GET /api/balances/{id}

POST

(a) create a balance

    POST /api/balances/create

PUT

(a) update an existing balance by id

    PUT /balances/{id}

DELETE

(a) delete a balance by id

    DEL /balances/{id}

(b) delete all balances

    DEL /balances/delete

II. TRANSACTIONS REST API

GET

(a) get all transactions by account number and greater than or equal to a start date and less than or equal to an end date with pagination
    and in descending order by transactionTs

    GET /api/transactions/accountNumber/{accountNumber}/fromDate/{fromDate}/toDate/{toDate}

(b) get transactions by account number and by type and greater than or equal to a from date and less than or equal to a to date 
    with pagination in descending order by transactionTs

    GET /api/transactions/accountNumber/{accountNumber}/type/{type}/fromDate/{fromDate}/toDate/{toDate}

(c) get transaction by id

    GET /api/transactions/{id}

(d) stream all transactions

    GET /api/stream/transactions

POST

(a) create a transaction

    POST /api/transactions/create

PUT

(a) update an existing transaction by id

    PUT /api/transactions/{id}

DELETE

(a) delete a transaction by id

    DEL /api/transactions/{id}

(b) delete all transactions

    DEL /api/transactions/delete

III. SAMPLE DATA

1. BALANCE

 {
   "accountNumber":"123456",
   "lastUpdateTimestamp":"2021-01-01T01:02:03.8Z",
   "balance":28.1
 }

 {
   "accountNumber":"123456",
   "lastUpdateTimestamp":"2021-02-02T01:02:03.8Z",
   "balance":70.3
 }

 {
   "accountNumber":"123456",
   "lastUpdateTimestamp":"2021-03-03T05:30:03.8Z",
   "balance":100.0
 }

 {
   "accountNumber":"7890",
   "lastUpdateTimestamp":"2021-04-01T05:30:03.8Z",
   "balance":87.1
 }

 {
   "accountNumber":"7890",
   "lastUpdateTimestamp":"2021-05-02T06:30:03.8Z",
   "balance":76.2
 }

 {
   "accountNumber":"7890",
   "lastUpdateTimestamp":"2021-06-03T07:30:03.8Z",
   "balance":500.99
 }

2. TRANSACTIONS

2.1 DEPOSIT

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-03T06:10:03.8Z",
    "type":"DEPOSIT",
    "amount": 75.2
  }

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-04T09:45:03.8Z",
    "type":"DEPOSIT",
    "amount": 45.2
  }

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-05T12:55:03.8Z",
    "type":"DEPOSIT",
    "amount": 65.2
  }

  {
    "accountNumber":"7890",
    "transactionTs":"2021-01-06T06:10:03.8Z",
    "type":"DEPOSIT",
    "amount": 39.1
  }

  {
    "accountNumber":"7890",
    "transactionTs":"2021-01-07T09:45:03.8Z",
    "type":"DEPOSIT",
    "amount": 59.2
  }

  {
    "accountNumber":"7890",
    "transactionTs":"2021-01-08T12:55:03.8Z",
    "type":"DEPOSIT",
    "amount": 42.7
  }

2.2 WITHDRAW

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-04T10:20:05.8Z",
    "type":"WITHDRAW",
    "amount":13.2
  }

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-05T11:20:05.8Z",
    "type":"WITHDRAW",
    "amount":84.6
  }

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-06T15:59:10.8Z",
    "type":"WITHDRAW",
    "amount":100.9
  }

  {
    "accountNumber":"7890",
    "transactionTs":"2021-01-07T16:20:05.8Z",
    "type":"WITHDRAW",
    "amount":13.2
  }

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-08T17:20:05.8Z",
    "type":"WITHDRAW",
    "amount":84.6
  }

  {
    "accountNumber":"123456",
    "transactionTs":"2021-01-09T18:59:10.8Z",
    "type":"WITHDRAW",
    "amount":100.9
  }

  {
    "accountNumber":"7890",
    "transactionTs":"2021-01-10T16:20:05.8Z",
    "type":"WITHDRAW",
    "amount":55.7
  }

  {
    "accountNumber":"7890",
    "transactionTs":"2021-01-28T16:20:05.8Z",
    "type":"WITHDRAW",
    "amount":849.3
  }

IV. HOW TO RUN THE PROGRAM

1. Dependencies 

(a) Spring Boot version 2.0.3.RELEASE
(b) MongoDB Reactive WebFlux
(c) Mockito version 1.9.5
(d) Apache commons lang version 2.6
(e) Java version 1.8
(f) Maven version 3.2.3
(g) Mongo DB NOSQL version 3.2

2. Import the project as an existing maven project to Eclipse or IntelliJ

3. To build the project execute this command from the command line

mvn clean install 

4. To run the project execute this command from the command line

mvn spring-boot:run
