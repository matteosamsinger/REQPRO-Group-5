package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private final String accountId;
    private String name;
    private String email;

    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<TopUp> topUps = new ArrayList<>();

    public Account(String accountId, String name, String email) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.balance = 0.0;
    }

    // --- Wallet

    public void topUp(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Top-up amount must be > 0");
        }
        balance += amount;
        TopUp topUp = new TopUp(topUps.size() + 1, amount);
        topUps.add(topUp);
    }

    public void debit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Debit amount must be >= 0");
        }
        if (balance < amount) throw new IllegalStateException("Insufficient balance");
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    // --- History ---

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<TopUp> getTopUps() {
        return Collections.unmodifiableList(topUps);
    }

    // --- Convenience Methoden

    public void topUpAccountWithMoney(double amount) {
        topUp(amount);
    }

    public double getAccountBalance() {
        return getBalance();
    }

    public List<TopUp> getBalanceTopUps() {
        return getTopUps();
    }

    // --- “Client-Daten” jetzt im Account ---

    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}