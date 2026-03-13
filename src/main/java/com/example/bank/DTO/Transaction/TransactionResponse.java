package com.example.bank.DTO.Transaction;

import java.math.BigDecimal;
import com.example.bank.enums.TransactionStatus;

public class TransactionResponse {
    private Long transactionId;
    private TransactionStatus status;
    private BigDecimal amount;
    private String referenceNumber;
    private String message;

    public TransactionResponse() {

    }

    public TransactionResponse(Long transactionId, TransactionStatus status, BigDecimal amount, String referenceNumber,
            String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.referenceNumber = referenceNumber;
        this.message = message;
    }

    // getters / setters

    public Long getTransactionId() {

        return transactionId;

    }

    public void setTransactionId(Long trnsactionId) {

        this.transactionId = transactionId;
        
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}