package org.example;

import java.time.LocalDateTime;

public class TopUp {

    private final int topUpId;
    private final LocalDateTime time;
    private final double amount;

    public TopUp(int topUpId, double amount) {
        this.topUpId = topUpId;
        this.amount = amount;
        this.time = LocalDateTime.now();
    }

    public int getTopUpId() {
        return topUpId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }
}

