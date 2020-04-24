package com.cwretirement.codetest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class ReconciliationService {
    private static final Logger LOG = LoggerFactory.getLogger(CodeTestApplication.class);

    @Value("${contribution.reconciliation.url}")
    private String contributionReconciliationURL;

    @Value("${trade.settlement.reconciliation.url}")
    private String tradeSettlementReconciliationURL;

    public void reconcileContribution(Contribution contribution) {
        URL url;
        try {
            url = new URL(contributionReconciliationURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000 * 5);
            connection.setReadTimeout(1000 * 5);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            final String jsonString = contribution.toString();
            byte[] representation = jsonString.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(representation.length);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            LOG.debug("Writing to remote service: {}", jsonString);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(representation, 0, representation.length);
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new ReconciliationException(ReconciliationException.Type.CANNOT_RECONCILE,
                        String.format("Reconciliation service returned HTTP code %d", responseCode));
            }
            // OK
        } catch (MalformedURLException e) {
            throw new RuntimeException("Configured URL for  was not well-formed", e);
        } catch (SocketTimeoutException e) {
            throw new ReconciliationException(ReconciliationException.Type.SERVICE_TIMEOUT);
        } catch (IOException e) {
            throw new ReconciliationException(ReconciliationException.Type.SERVICE_UNAVAILABLE, e);
        }
    }

    public void reconcileTradeSettlement(TradeSettlement tradeSettlement) {
        // TODO: fill in
    }
}