package org.example.stepdefinitions.account;

import io.cucumber.java.en.Given;
import org.example.entities.Account;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountBalanceSteps {

    @Given("account {string} has balance {double} EUR")
    public void accountHasBalance(String accountId, double amount) {
        Account acc = CTX.getNetwork().findAccount(accountId);
        assertNotNull(acc, "Account not found: " + accountId);
        acc.topUp(amount);
    }
}
