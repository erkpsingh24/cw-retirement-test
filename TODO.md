<!-- Please add further comments, questions, and improvements in this file -->

###### **Changes done:**

1.  I have used the lombok dependency and using the builder pattern to create our model objects when required, since too many constructor arguments might not be a great idea and builder pattern provides flexibility in case some of the fields are non-mandatory.
2.  Refactored the project structure moving different classes to relative packages for separation of concerns
3.  Created a TransactionService and moved the file parsing logic there to abstract it out of the main application class.
4.  Wrote some parsing test cases for contribution and settlement type of transactions.

 
###### **Few TODO notes for improvements :**

1.  Right now while processing the file, we are reading each line, parsing it into a Transaction, converting a transaction into a Contribution or Trade and calling the reconcilation service.
    Depending on the file size and the specific use case, we can probably read the file at once and keep the transactions in memory to reduce the IO context-switch 
    This however might be error prone, in case our application goes down, we would lose the in-memory transactions and will need to read the file again when application restarts and reprocessing might 
    cause idempotency issues, causing one transaction to be processed multiple times.
    (Idempotency might not be as important here, if the assumed purpose of this application is only reconcilation)
    
2.  We can have a certain number of retries in case we get a failure from the remote service, with a time interval between multiple calls

3.  I personally am more accustomed to using a FeignClient for remote calls, would love to apply this as an improvement.

    This however can be error prone, in case the application goes down, we would lose the in-memory transactions and will need to read the file afresh when application restarts and reprocessing might 
    cause idempotency issues, causing one transaction to be processed multiple times.
    
    (Idempotency might not be as important here, if the assumed purpose of this application is only reconcilation)
    
2.  We can have a certain number of retries in case we get a failure from the remote service, with a time interval between multiple calls, Again this can cause issues in idempotency sensitive applications.

3.  I personally will be interested to using a FeignClient for remote service calls, would love to apply this as an improvement.
    easier 

4.  If we have the remote service functionality exactly identified we can mock its responses in our test cases and cover different possibilities related to the remote call.

