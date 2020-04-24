package com.cwretirement.codetest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TradeSettlement {

    private Type type;
    private Date dateEntered;
    private Date datePosted;
    private Date dateSettled;
    private Date dateTraded;
    private BigDecimal units;
    private BigDecimal unitPrice;
    private String fundName;
    private BigDecimal amount;
    private String symbolCusip;
    private long custodianReference;


    public enum Type {
        PURCHASE,
        SALE;

        private static final Map<String, Type> TRANSACTION_DESCRIPTION_TO_SETTLEMENT_TYPE = new HashMap<String, Type>() {{
            put("Purchase Cash Settlement", PURCHASE);
            put("Sale Cash Settlement", SALE);
        }};

        public static Type fromString(String string) {
            return TRANSACTION_DESCRIPTION_TO_SETTLEMENT_TYPE.get(string);
        }
    }



}
