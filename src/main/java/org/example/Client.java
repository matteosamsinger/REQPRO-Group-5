package org.example;

import java.util.Collections;
import java.util.List;

public class Client {

    private final String clientId;
    private String name;
    private String email;
    private final Account account;

    public Client(String clientId, String name, String email, Account account) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.account = account;
    }

    public String getClientId() {
        return clientId;
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

    public Account getAccount() {
        return account;
    }



    public double getBalance() {
        return account != null ? account.getBalance() : 0.0;
    }

    public List<Transaction> getTransactions() {
        return account != null ? account.getTransactions() : Collections.emptyList();
    }

    public List<TopUp> getTopUps() {
        return account != null ? account.getTopUps() : Collections.emptyList();
    }
}
