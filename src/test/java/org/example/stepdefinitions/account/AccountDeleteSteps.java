package org.example.stepdefinitions.account;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AccountDeleteSteps {

    @When("I delete the account {string}")
    public void iDeleteTheAccount(String accountId) {
        CTX.clearLastException();
        try {
            CTX.getNetwork().deleteAccount(accountId);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("there should be no account with id {string}")
    public void thereShouldBeNoAccountWithId(String accountId) {
        Account acc = CTX.getNetwork().findAccount(accountId); // returns null if not found
        assertNull(acc, "Account should be deleted/not exist, but was found: " + accountId);
    }
}
