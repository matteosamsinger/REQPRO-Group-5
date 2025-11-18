package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<TopUp> topUps = new ArrayList<>();

    public Account() {
        this.balance = 0.0;
    }

    public void topUp(double amount) {
        balance += amount;
        TopUp topUp = new TopUp(topUps.size() + 1, amount);
        topUps.add(topUp);
    }

    public void debit(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<TopUp> getTopUps() {
        return Collections.unmodifiableList(topUps);
    }
}
