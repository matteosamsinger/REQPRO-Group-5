package org.example;

import java.time.LocalDateTime;

public class Transaction {

    private final int transactionId;
    private final int positionNumber;
    private final double amount;
    private final LocalDateTime startTime;

    public Transaction(int transactionId, int positionNumber, double amount) {
        this.transactionId = transactionId;
        this.positionNumber = positionNumber;
        this.amount = amount;
        this.startTime = LocalDateTime.now();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getPositionNumber() {
        return positionNumber;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
