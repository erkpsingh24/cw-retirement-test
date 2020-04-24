package com.cwretirement.codetest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CodeTestApplicationE2ETest {

    @Autowired
    CodeTestApplication codeTestApplication;

    @MockBean
    private ReconciliationService reconciliationService;

    @Test
    void runWholeApplication() throws ParseException {
        ArgumentCaptor<Contribution> contributionArgumentCaptor = ArgumentCaptor.forClass(Contribution.class);
        verify(reconciliationService, times(2)).reconcileContribution(contributionArgumentCaptor.capture());
        final List<Contribution> contributionList = contributionArgumentCaptor.getAllValues();


        final Date jan31 = Transaction.DATE_FORMAT.parse("31-Jan-20");
        Contribution contribution1 = new Contribution(321095, "cf4f9942-3c52-41dd-978d-ba90a3675f9c", jan31, jan31, new BigDecimal("10000.00"), 4459254);
        Contribution contribution2 = new Contribution(123005, "a2ef9542-3c52-b16a-9f8f-345aa387520c", jan31, jan31, new BigDecimal("200.00"), 4459687);
        assertThat(contributionList).hasSize(2).containsExactly(contribution1, contribution2);
    }

}
