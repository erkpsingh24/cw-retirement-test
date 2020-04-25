package com.cwretirement.codetest.service;

import com.cwretirement.codetest.model.Contribution;
import com.cwretirement.codetest.model.TradeSettlement;
import com.cwretirement.codetest.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.stream.Stream;

@Service
public class TransactionService {
  private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

  @Autowired
  ReconciliationService reconciliationService;

  public void readTransactionsFromFile(Path path) {
    Stream<String> lines;
    try {
      lines = Files.lines(path);
      lines.skip(1).forEach(
              line -> {
                LOG.debug("Read line: {}", line);
                String[] fields = line.split("\\|");
                Transaction transaction = null;
                try {
                  transaction = new Transaction(fields);
                } catch (ParseException e) {
                  LOG.error("Failed to parse transaction: ", e);
                }
                LOG.debug("Parsed transaction: {}", transaction.toString());
                switch (transaction.getType()) {
                  case CONTRIBUTION:
                    {
                      Contribution contribution = Contribution.fromTransaction(transaction);
                      reconciliationService.reconcileContribution(contribution);
                      break;
                    }
                  case PURCHASE_TRADE:
                  case SALE_TRADE:
                    {
                      TradeSettlement tradeSettlement = TradeSettlement.fromTransaction(transaction);
                      reconciliationService.reconcileTradeSettlement(tradeSettlement);
                      break;
                    }
                  default:
                    {
                      LOG.error("Incorrect transaction type, cannot process");
                      //TODO: can throw a specific exception here for ease of debugging
                    }
                }
              });

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
