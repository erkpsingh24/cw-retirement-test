package com.cwretirement.codetest;

import com.cwretirement.codetest.model.Contribution;
import com.cwretirement.codetest.model.TradeSettlement;
import com.cwretirement.codetest.model.Transaction;
import com.cwretirement.codetest.service.ReconciliationService;
import com.cwretirement.codetest.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.cwretirement.codetest.model.TradeSettlement.Type.PURCHASE;
import static com.cwretirement.codetest.model.TradeSettlement.Type.SALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CodeTestApplicationE2ETest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    CodeTestApplication codeTestApplication;

    @MockBean
    private ReconciliationService reconciliationService;

    @Test
    void runWholeApplication() throws ParseException {
        doNothing().when(reconciliationService).reconcileContribution(any(Contribution.class));
        ArgumentCaptor<Contribution> contributionArgumentCaptor = ArgumentCaptor.forClass(Contribution.class);
        transactionService.readTransactionsFromFile(Paths.get("/Users/karanpreetsingh/Downloads/cw-server-code-test/src/test/resources/transactions-test-file.txt"));
        verify(reconciliationService, times(2)).reconcileContribution(contributionArgumentCaptor.capture());
        final List<Contribution> contributionList = contributionArgumentCaptor.getAllValues();


        final Date jan31 = Transaction.DATE_FORMAT.parse("31-Jan-20");
        Contribution contribution1 = new Contribution(321095, "cf4f9942-3c52-41dd-978d-ba90a3675f9c", jan31, jan31, new BigDecimal("10000.00"), 4459254);
        Contribution contribution2 = new Contribution(123005, "a2ef9542-3c52-b16a-9f8f-345aa387520c", jan31, jan31, new BigDecimal("200.00"), 4459687);
        assertThat(contributionList).hasSize(2).containsExactly(contribution1, contribution2);
    }

    @Test
    void testContributionParsing(){
        Date date = new Date();
        Transaction transaction = Transaction.builder()
                                             .acct(111111)
                                             .acctNm("Common Wealth CAD")
                                             .entered(date)
                                             .posted(date)
                                             .dateSettled(date)
                                             .dateTraded(date)
                                             .transactionDescription("Contribution")
                                             .quantity(new BigDecimal(0.0000))
                                             .rate(new BigDecimal(0.0000))
                                             .descr("cf4f9942-3c52-41dd-978d-ba90a3675f9c")
                                             .amount(new BigDecimal(10000.00))
                                             .symbolCusip("")
                                             .explanation("000321095")
                                             .job(4459254)
                                             .type(Transaction.Type.CONTRIBUTION)
                                             .build();

    assertEquals(Contribution.builder().memberAccountId(321095)
                           .transactionId("cf4f9942-3c52-41dd-978d-ba90a3675f9c")
                           .dateEntered(date)
                           .datePosted(date)
                           .amount(new BigDecimal(10000.00))
                           .custodianReference(4459254).build()
          ,Contribution.fromTransaction(transaction));


    }

    @Test
    void testTradeSettlementParsing(){
        Date date = new Date();
        Transaction purchaseTransaction = Transaction.builder()
                                             .acct(111111)
                                             .acctNm("Common Wealth CAD")
                                             .entered(date)
                                             .posted(date)
                                             .dateSettled(date)
                                             .dateTraded(date)
                                             .transactionDescription("Purchase Cash Settlement")
                                             .quantity(new BigDecimal(0.0000))
                                             .rate(new BigDecimal(0.0000))
                                             .descr("cf4f9942-3c52-41dd-978d-ba90a3675f9c")
                                             .amount(new BigDecimal(10000.00))
                                             .symbolCusip("")
                                             .explanation("000321095")
                                             .job(4459254)
                                             .type(Transaction.Type.PURCHASE_TRADE)
                                             .build();


        assertEquals(TradeSettlement.builder().type(PURCHASE)
                .dateEntered(date)
                .datePosted(date)
                .dateSettled(date)
                .dateTraded(date)
                .units(new BigDecimal(0))
                .unitPrice(new BigDecimal(0))
                .fundName("cf4f9942-3c52-41dd-978d-ba90a3675f9c")
                .amount(new BigDecimal(10000.00))
                .symbolCusip("")
                .custodianReference(4459254).build(),
                TradeSettlement.fromTransaction(purchaseTransaction));

        Transaction saleTransaction = Transaction.builder()
                                             .acct(111111)
                                             .acctNm("Common Wealth CAD")
                                             .entered(date)
                                             .posted(date)
                                             .dateSettled(date)
                                             .dateTraded(date)
                                             .transactionDescription("Sale Cash Settlement")
                                             .quantity(new BigDecimal(0.0000))
                                             .rate(new BigDecimal(0.0000))
                                             .descr("cf4f9942-3c52-41dd-978d-ba90a3675f9c")
                                             .amount(new BigDecimal(10000.00))
                                             .symbolCusip("")
                                             .explanation("000321095")
                                             .job(4459254)
                                             .type(Transaction.Type.SALE_TRADE)
                                             .build();

        assertEquals(TradeSettlement.builder().type(SALE)
                                           .dateEntered(date)
                                           .datePosted(date)
                                           .dateSettled(date)
                                           .dateTraded(date)
                                           .units(new BigDecimal(0))
                                           .unitPrice(new BigDecimal(0))
                                           .fundName("cf4f9942-3c52-41dd-978d-ba90a3675f9c")
                                           .amount(new BigDecimal(10000.00))
                                           .symbolCusip("")
                                           .custodianReference(4459254).build(),
                TradeSettlement.fromTransaction(saleTransaction));
    }

}
