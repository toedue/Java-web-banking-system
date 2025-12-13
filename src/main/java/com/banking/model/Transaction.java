package com.banking.model;

import java.util.Date;

public class Transaction {
    private int id;
    private String transactionType;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private double amount;
    private String note;
    private Date createdAt;
    
    public Transaction() {}
    
    public Transaction(int id, String transactionType, String senderAccountNumber, 
                      String receiverAccountNumber, double amount, String note, Date createdAt) {
        this.id = id;
        this.transactionType = transactionType;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.note = note;
        this.createdAt = createdAt;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public String getSenderAccountNumber() { return senderAccountNumber; }
    public void setSenderAccountNumber(String senderAccountNumber) { this.senderAccountNumber = senderAccountNumber; }
    
    public String getReceiverAccountNumber() { return receiverAccountNumber; }
    public void setReceiverAccountNumber(String receiverAccountNumber) { this.receiverAccountNumber = receiverAccountNumber; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

