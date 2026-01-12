package org.example.steps.InvoiceSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.example.entities.Account;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceReadSteps {
    private final ScenarioContext ctx;

    public InvoiceReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @And("I request the invoice status for client {string}")
    public void iRequestTheInvoiceStatusForClient(String clientId) {
        Account account = ctx.network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);
    }

    @Then("the invoice for client {string} should contain {int} top-ups and {int} charging transaction")
    public void theInvoiceForClientShouldContainTopUpsAndChargingTransaction(String clientId, int expectedTopUps, int expectedTransactions) {
        Account account = ctx.network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);

        assertEquals(expectedTopUps, account.getTopUps().size());
        assertEquals(expectedTransactions, account.getInvoiceLineItems().size());
    }
}

