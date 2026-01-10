package org.example.entities;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Account {

    private final String accountId;
    private String name;
    private String email;

    private double balance;
    private final List<InvoiceLineItem> invoiceItems = new ArrayList<>();

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

    // --- Invoice items

    public void createInvoiceLineItem(InvoiceLineItem item) {
        invoiceItems.add(item);
    }

    public List<InvoiceLineItem> getInvoiceLineItems() {
        return Collections.unmodifiableList(invoiceItems);
    }

    // --- TopUps

    public List<TopUp> getTopUps() {
        return Collections.unmodifiableList(topUps);
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

    // Invoice

    public String toInvoiceString() {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        sb.append("INVOICE for Account ").append(accountId)
                .append(" | name=").append(name)
                .append(" | email=").append(email).append(nl);

        sb.append(nl).append("Charging invoice items (sorted by start time):").append(nl);

        List<InvoiceLineItem> items = new ArrayList<>(invoiceItems);
        items.sort(Comparator.comparing(InvoiceLineItem::getStartTime));

        if (items.isEmpty()) {
            sb.append("  (none)").append(nl);
        } else {
            sb.append(String.format(
                    "%-4s %-16s %-18s %-7s %-4s %5s %7s %10s%n",
                    "Pos", "Start", "Location", "Charger", "Mode", "Min", "kWh", "Price(EUR)"
            ));
            sb.append("--------------------------------------------------------------------------").append(nl);

            int pos = 1;
            for (InvoiceLineItem it : items) {
                sb.append(String.format(Locale.US,
                        "%-4d %-16s %-18.18s %-7s %-4s %5d %7.2f %10.2f%n",
                        pos++,
                        it.getStartTime().format(dtf),
                        it.getLocationName(),          // wird auf 18 Zeichen gekürzt
                        it.getChargerNumber(),
                        it.getMode(),
                        it.getDurationMinutes(),
                        it.getEnergyKWh(),
                        it.getPriceEur()
                ));
            }
        }

        sb.append(nl).append("Top-ups:").append(nl);
        if (topUps.isEmpty()) {
            sb.append("  (none)").append(nl);
        } else {
            for (TopUp t : topUps) {
                sb.append(String.format(Locale.US,
                        "  - #%d | %s | %.2f EUR%n",
                        t.getTopUpId(),
                        t.getTime().format(dtf),
                        t.getAmount()
                ));
            }
        }

        sb.append(nl).append(String.format(Locale.US, "Current balance: %.2f EUR%n", balance));
        return sb.toString();
    }
}
