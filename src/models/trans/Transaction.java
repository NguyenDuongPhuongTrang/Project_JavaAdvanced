package models.trans;

import enums.TransType;

public class Transaction {
    private String transactionId;
    private String customerId;
    private double amount;
    private TransType type;
    private String description;
    private String createdAt;

    public Transaction(String transactionId, String customerId, double amount, TransType type, String description, String createdAt) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransType getType() {
        return type;
    }

    public void setType(TransType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
