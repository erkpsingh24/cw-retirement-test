package com.cwretirement.codetest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.stream.Stream;

@SpringBootApplication
public class CodeTestApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CodeTestApplication.class);
    @Autowired
    ReconciliationService reconciliationService;
    @Value("${transaction.file.path}")
    private Path path;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CodeTestApplication.class);
        app.run(args);
    }


    @Override
    public void run(String... args) {
        Stream<String> lines;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lines.skip(1).forEach(line -> {
            LOG.debug("Read line: {}", line);
            String[] fields = line.split("\\|");
            Transaction transaction = null;
            try {
                transaction = new Transaction(fields);
            } catch (ParseException e) {
                LOG.error("Failed to parse transaction: ", e);
            }
            LOG.debug("Parsed transaction: {}", transaction.toString());
            if (Transaction.Type.CONTRIBUTION.equals(transaction.getType())) {
                Contribution contribution = transaction.toContribution();
                reconciliationService.reconcileContribution(contribution);
            }
        });
    }
}
