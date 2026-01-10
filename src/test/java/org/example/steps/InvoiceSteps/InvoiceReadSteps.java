package org.example.steps.InvoiceSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.example.Account;
import org.example.Client;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceReadSteps {
    private final ScenarioContext ctx;

    @And("I request the invoice status for client {string}")
    public void iRequestTheInvoiceStatusForClient(String clientId) {
        Client client = ctx.network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);
        // Nichts weiter nötig – ausgewertet wird im Then-Step
    }


    @Then("the invoice for client {string} should contain {int} top-ups and {int} charging transaction")
    public void theInvoiceForClientShouldContainTopUpsAndChargingTransaction(String clientId, int expectedTopUps, int expectedTransactions) {
        Client client = ctx.network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Client has no account: " + clientId);

        assertEquals(expectedTopUps, account.getTopUps().size());
        assertEquals(expectedTransactions, account.getTransactions().size());
    }

    public InvoiceReadSteps(ScenarioContext ctx)
    {
        this.ctx=ctx;
    }
}
