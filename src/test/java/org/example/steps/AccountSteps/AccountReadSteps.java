package org.example.steps.AccountSteps;

import io.cucumber.java.en.Then;
import org.example.entities.Account;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;

public class AccountReadSteps {

    private final ScenarioContext ctx;

    public AccountReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Then("there should be a client with id {string} and name {string}")
    public void thereShouldBeAClientWithIdAndName(String clientId, String expectedName) {
        Account acc = ctx.network.findAccount(clientId);
        assertNotNull(acc);
        assertEquals(expectedName, acc.getName());
    }

    @Then("the client {string} should have an account with balance 0 EUR")
    public void theClientShouldHaveAnAccountWithBalance0Eur(String clientId) {
        Account acc = ctx.network.findAccount(clientId);
        assertNotNull(acc);
        assertEquals(0.0, acc.getBalance(), 0.0001);
    }

    @Then("the account of client {string} should have a balance of {double} EUR")
    public void theAccountOfClientShouldHaveABalanceOfEur(String clientId, double expectedBalance) {
        Account acc = ctx.network.findAccount(clientId);
        assertNotNull(acc);
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }
}

