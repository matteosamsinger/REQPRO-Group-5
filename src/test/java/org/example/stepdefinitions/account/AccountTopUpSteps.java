package org.example.stepdefinitions.account;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTopUpSteps {

    @When("I top up account {string} by {double} EUR")
    public void iTopUpAccountByEur(String accountId, double amount) {
        CTX.clearLastException();
        try {
            Account acc = CTX.getNetwork().findAccount(accountId);
            assertNotNull(acc, "Account not found: " + accountId);

            acc.topUp(amount);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("the account {string} should have {int} top-up")
    public void theAccountShouldHaveTopUps(String accountId, int expectedCount) {
        Account acc = CTX.getNetwork().findAccount(accountId);
        assertNotNull(acc, "Account not found: " + accountId);

        assertEquals(expectedCount, acc.getTopUps().size());
    }
}
