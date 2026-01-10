package org.example.managers;

import org.example.entities.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    private final Map<String, Account> accounts = new HashMap<>();

    //create
    public void createAccount(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    //read
    public Account readAccount(String accountId) {
        return accounts.get(accountId);
    }

    public Collection<Account> readAllAccounts() {
        return accounts.values();
    }

    public void deleteAccount(String accountId) {
        accounts.remove(accountId);
    }


}