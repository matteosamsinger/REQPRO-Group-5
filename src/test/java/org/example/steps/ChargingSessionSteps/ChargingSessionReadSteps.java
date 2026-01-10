package org.example.steps.ChargingSessionSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.example.Account;
import org.example.Client;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargingSessionReadSteps {
    private final ScenarioContext ctx;

    @Then("the charging session should have a duration of {int} minutes")
    public void theChargingSessionShouldHaveADurationOfMinutes(int expectedMinutes) {
        assertNotNull(ctx.currentSession, "No current charging session");
        assertEquals(expectedMinutes, ctx.currentSession.getDurationMinutes());
    }


    @And("the total price of the charging session should be {double} EUR")
    public void theTotalPriceOfTheChargingSessionShouldBeEur(double expectedPrice) {
        assertNotNull(ctx.currentSession, "No current charging session");
        assertEquals(expectedPrice, ctx.currentSession.getTotalPrice(), 0.0001);
    }


    @And("the account balance of client {string} should be {double} EUR")
    public void theAccountBalanceOfClientShouldBeEur(String clientId, double expectedBalance) {
        Client client = ctx.network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Account for client not found: " + clientId);

        assertEquals(expectedBalance, account.getBalance(), 0.0001);
    }

    public ChargingSessionReadSteps(ScenarioContext ctx)
    {
        this.ctx=ctx;
    }
}
