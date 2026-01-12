package org.example.stepdefinitions.account;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountReadSteps {

    private Account lastReadAccount;

    @When("I read the account {string}")
    public void iReadTheAccount(String accountId) {
        CTX.clearLastException();
        lastReadAccount = null;

        try {
            lastReadAccount = CTX.getNetwork().findAccount(accountId);

            if (lastReadAccount == null) {
                throw new IllegalArgumentException("Account not found: " + accountId);
            }
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("I should see account id {string} name {string} and email {string}")
    public void iShouldSeeAccount(String id, String expectedName, String expectedEmail) {
        Account acc = CTX.getNetwork().findAccount(id);
        assertNotNull(acc, "Account not found: " + id);

        assertEquals(id, acc.getAccountId());
        assertEquals(expectedName, acc.getName());
        assertEquals(expectedEmail, acc.getEmail());
    }

    @Given("an account {string} exists")
    public void anAccountExists(String accountId) {
        CTX.clearLastException();
        try {
            Account acc = CTX.getNetwork().findAccount(accountId); // oder readAccount(...) bei dir
            if (acc == null) {
                throw new IllegalArgumentException("Account not found: " + accountId);
            }
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }
}
