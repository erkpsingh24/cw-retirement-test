package com.cwretirement.codetest;

import com.cwretirement.codetest.service.ReconciliationService;
import com.cwretirement.codetest.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;

@SpringBootApplication
public class CodeTestApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CodeTestApplication.class);

    @Autowired
    private TransactionService transactionService;
    @Value("${transaction.file.path}")
    private Path path;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CodeTestApplication.class);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        transactionService.readTransactionsFromFile(path);
    }
}
