package org.example.steps.AccountSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.example.entities.Account;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;

public class AccountUpdateSteps {

    private final ScenarioContext ctx;

    public AccountUpdateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @When("I top up the account of client {string} by {double} EUR")
    public void iTopUpTheAccountOfClientByEur(String clientId, double amount) {
        Account acc = ctx.network.findAccount(clientId);
        assertNotNull(acc);
        acc.topUp(amount);
    }

    @And("the account of client {string} has a balance of {double} EUR")
    public void theAccountOfClientHasABalanceOfEur(String clientId, double amount) {
        Account acc = ctx.network.findAccount(clientId);
        assertNotNull(acc);

        // wenn dieser Assert bei euch nervt/inkonsistent ist, löschen:
        assertEquals(0.0, acc.getBalance(), 0.0001);

        acc.topUp(amount);
    }
}
