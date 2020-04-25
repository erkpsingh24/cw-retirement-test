package com.cwretirement.codetest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TradeSettlement that = (TradeSettlement) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(custodianReference, that.custodianReference)
                .append(type, that.type)
                .append(dateEntered, that.dateEntered)
                .append(datePosted, that.datePosted)
                .append(dateSettled, that.dateSettled)
                .append(dateTraded, that.dateTraded)
                .append(units, that.units)
                .append(unitPrice, that.unitPrice)
                .append(fundName, that.fundName)
                .append(amount, that.amount)
                .append(symbolCusip, that.symbolCusip)
                .isEquals();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static TradeSettlement fromTransaction(Transaction transaction){ //TODO: We can use Optional to handle the possible null fields values if any
        assert Transaction.Type.PURCHASE_TRADE.equals(transaction.getType()) || Transaction.Type.SALE_TRADE.equals(transaction.getType());

        return TradeSettlement.builder()
                       .amount(transaction.getAmount())
                       .type(Type.fromString(transaction.getTransactionDescription()))
                       .custodianReference(transaction.getJob())
                       .dateEntered(transaction.getEntered())
                       .datePosted(transaction.getPosted())
                       .dateTraded(transaction.getDateTraded())
                       .dateSettled(transaction.getDateSettled())
                       .unitPrice(transaction.getRate())
                       .units(transaction.getQuantity())
                       .fundName(transaction.getDescr())
                       .symbolCusip(transaction.getSymbolCusip())
                       .build();
    }

}
