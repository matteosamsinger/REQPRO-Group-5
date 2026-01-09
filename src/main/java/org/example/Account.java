package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Account {

    private final String accountId;
    private String name;
    private String email;

    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<TopUp> topUps = new ArrayList<>();

    private final List<InvoiceLineItem> invoiceItems = new ArrayList<>();

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

    // --- Invoice ---

    public void addInvoiceItem(InvoiceLineItem item) {
        invoiceItems.add(item);
    }

    public List<InvoiceLineItem> getInvoiceItems() {
        return Collections.unmodifiableList(invoiceItems);
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


    public String toInvoiceString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"/*"dd.MM.yyyy HH:mm"*/);
        String nl = System.lineSeparator();

        StringBuilder sb = new StringBuilder();
        sb.append("INVOICE STATUS").append(nl);
        sb.append("Account: ").append(accountId)
                .append(" | ").append(name)
                .append(" | ").append(email).append(nl);
        sb.append("Generated: ").append(LocalDateTime.now().format(fmt)).append(nl);
        sb.append(nl);

        // 1) Rechnungsposten (Ladevorgänge) sortiert nach Startzeit
        sb.append("CHARGING ITEMS (sorted by start time)").append(nl);

        if (invoiceItems.isEmpty()) {
            sb.append("  (none)").append(nl);
        } else {
            invoiceItems.stream()
                    .sorted(Comparator.comparing(InvoiceLineItem::getStartTime))
                    .forEach(item -> sb.append("  ")
                            .append(item.getPositionNumber()).append(". ")
                            .append(item.getStartTime().format(fmt))
                            .append(" | ").append(item.getLocationName())
                            .append(" | charger ").append(item.getChargerNumber())
                            .append(" | ").append(item.getMode())
                            .append(" | ").append(item.getDurationMinutes()).append(" min")
                            .append(" | ").append(String.format(Locale.US, "%.2f", item.getEnergyKWh())).append(" kWh")
                            .append(" | ").append(String.format(Locale.US, "%.2f", item.getPriceEur())).append(" EUR")
                            .append(nl));
        }

        sb.append(nl);

        // 2) TopUps
        sb.append("TOP-UPS").append(nl);
        if (topUps.isEmpty()) {
            sb.append("  (none)").append(nl);
        } else {
            for (TopUp t : topUps) {
                sb.append("  #").append(t.getTopUpId())
                        .append(" | ").append(t.getTime().format(fmt))
                        .append(" | +").append(String.format(Locale.US, "%.2f", t.getAmount())).append(" EUR")
                        .append(nl);
            }
        }

        sb.append(nl);

        // 3) Offenes Guthaben
        sb.append("OPEN BALANCE: ").append(String.format(Locale.US, "%.2f", balance)).append(" EUR").append(nl);

        return sb.toString();
    }
}