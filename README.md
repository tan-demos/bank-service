# Homework 

1. API is defined in package [bank-service-api](https://github.com/tan-demos/bank-service-api) 
2. Unit test report is under /build/reports/tests 
3. Integration tests are another separate package [bank-service-tests](https://github.com/tan-demos/bank-service-api) 
4. Integration test report is under bank-service-tests/build/reports/tests 
5. Transaction id 
    type is `long` and `BIGSERIAL` in PSQL database, `snowflake id` or `UUID` are recommended in production
6. Retry mechanism 
    Current design relies on the client to retry.
   1) Client call `createTransaction` to generate a `PENDING` transaction in backend database
   2) Client call `submitTransaction` with the `id` returned by `createTransaction` to execute the transaction
   3) `submitTransaction` is idempotent, client can always to retry safely in case of communication issue or server internal errors. 
7. For the transaction type, current implementation Only supports `TRANSFER`, it doesn't support `DEPOSIT` or `WITHDRAWAL`.  
8. To avoid deadlock in db transactions, it's better to limit db transaction wait timeout. Current implementation also set db transaction timeout in class `TransactionExecutor` 
   
####Sorry, I was a little busy in the past week, didn't get enough time to work on this task.   

To-do: 
1. Deploy and test in K8S cluster 
2. Resilience testing
3. Performance testing