package org.example.steps.InvoiceSteps;

import io.cucumber.java.en.And;
import org.example.entities.Account;
import org.example.entities.InvoiceLineItem;
import org.example.enums.ChargerType;
import org.example.steps.support.ScenarioContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceCreateSteps {
    private final ScenarioContext ctx;

    public InvoiceCreateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @And("I add a charging transaction of {int} EUR for client {string}")
    public void iAddAChargingTransactionOfEURForClient(int amount, String clientId) {
        Account account = ctx.network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);

        // Balance abbuchen
        account.debit(amount);

        // Als "charging transaction" auf die Rechnung schreiben
        int nextPos = account.getInvoiceLineItems().size() + 1;
        InvoiceLineItem item = new InvoiceLineItem(
                nextPos,
                LocalDateTime.now(),
                "n/a",
                "n/a",
                ChargerType.AC,
                0,
                0.0,
                amount
        );
        account.createInvoiceLineItem(item);
    }
}

