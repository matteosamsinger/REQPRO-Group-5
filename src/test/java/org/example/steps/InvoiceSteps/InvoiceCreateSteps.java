package org.example.steps.InvoiceSteps;

import io.cucumber.java.en.And;
import org.example.Account;
import org.example.Client;
import org.example.Transaction;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceCreateSteps {
    private final ScenarioContext ctx;

    @And("I add a charging transaction of {int} EUR for client {string}")
    public void iAddAChargingTransactionOfEURForClient(int amount, String clientId) {
        Client client = ctx.network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Client has no account: " + clientId);

        int nextId = account.getTransactions().size() + 1;
        Transaction tx = new Transaction(nextId, nextId, amount);

        account.debit(amount);
        account.addTransaction(tx);
    }

    public InvoiceCreateSteps(ScenarioContext ctx)
    {
        this.ctx = ctx;
    }
}
