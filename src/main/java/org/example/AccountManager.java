package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    private final Map<String, Account> accounts = new HashMap<>();

    public void registerAccount(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    public Account findAccount(String accountId) {
        return accounts.get(accountId);
    }

    public void deleteAccount(String accountId) {
        accounts.remove(accountId);
    }

    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }
}