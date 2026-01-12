package org.example.stepdefinitions.invoice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;

import java.util.regex.Pattern;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.*;

public class ClientInvoiceSteps {

    private String lastInvoice;

    @When("I read the client invoice for account {string}")
    public void iReadTheClientInvoiceForAccount(String accountId) {
        CTX.clearLastException();
        lastInvoice = null;

        try {
            Account acc = CTX.getNetwork().findAccount(accountId);
            if (acc == null) {
                throw new IllegalArgumentException("Account not found: " + accountId);
            }

            lastInvoice = acc.toInvoiceString();

        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("the invoice should be for account {string} name {string} email {string}")
    public void theInvoiceShouldBeForAccount(String id, String name, String email) {
        assertNotNull(lastInvoice, "No invoice was read");
        String header = "INVOICE for Account " + id + " | name=" + name + " | email=" + email;
        assertTrue(lastInvoice.contains(header),
                "Expected invoice header missing.\nExpected:\n" + header + "\n\nInvoice was:\n" + lastInvoice);
    }

    @Then("the invoice should show {int} top-up")
    public void theInvoiceShouldShowTopUps(int expectedCount) {
        assertNotNull(lastInvoice, "No invoice was read");

        // Block "Top-ups:" extrahieren
        int start = lastInvoice.indexOf("\nTop-ups:");
        assertTrue(start >= 0, "Missing 'Top-ups:' section.\nInvoice was:\n" + lastInvoice);

        int end = lastInvoice.indexOf("\nCurrent balance:", start);
        String block = (end >= 0) ? lastInvoice.substring(start, end) : lastInvoice.substring(start);

        // Wenn expectedCount==0 -> muss "(none)" drin sein
        if (expectedCount == 0) {
            assertTrue(block.contains("(none)"),
                    "Expected no top-ups but '(none)' missing.\nTop-up block was:\n" + block);
            return;
        }

        // sonst: zähle Zeilen die mit "  - #" starten
        int count = 0;
        for (String line : block.split("\\R")) {
            if (line.trim().startsWith("- #")) {
                count++;
            }
        }

        assertEquals(expectedCount, count,
                "Unexpected number of top-ups.\nTop-up block was:\n" + block);
    }

    @Then("the invoice should show {int} charging invoice item")
    public void theInvoiceShouldShowChargingInvoiceItems(int expectedCount) {
        assertNotNull(lastInvoice, "No invoice was read");

        // Zählt Tabellenzeilen wie:
        // "1    2026-01-10 10:00 ..."
        Pattern rowPattern = Pattern.compile("^\\s*\\d+\\s+\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}.*$");

        int count = 0;
        for (String line : lastInvoice.split("\\R")) {
            if (rowPattern.matcher(line).matches()) {
                count++;
            }
        }

        // Wenn ihr bei 0 Items "(none)" ausgebt, stellen wir zusätzlich sicher, dass das auch drin ist
        if (expectedCount == 0) {
            assertTrue(lastInvoice.contains("(none)"),
                    "Expected '(none)' for empty invoice items.\nInvoice was:\n" + lastInvoice);
        } else {
            assertFalse(lastInvoice.contains("(none)"),
                    "Did not expect '(none)' when items exist.\nInvoice was:\n" + lastInvoice);
        }

        assertEquals(expectedCount, count,
                "Unexpected number of charging invoice items.\nInvoice was:\n" + lastInvoice);
    }
}
