package org.example.stepdefinitions.account;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccoutSteps {

    @Given("an account with id {string} name {string} and email {string} exists")
    public void anAccountExists(String accountId, String name, String email) {
        Account acc = new Account(accountId, name, email);
        CTX.getNetwork().createAccount(acc);
    }

    @When("I create an account with id {string} name {string} and email {string}")
    public void iCreateAnAccount(String accountId, String name, String email) {
        CTX.clearLastException();
        try {
            Account acc = new Account(accountId, name, email);
            CTX.getNetwork().createAccount(acc);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("there should be an account with id {string} and name {string}")
    public void thereShouldBeAnAccount(String accountId, String expectedName) {
        Account acc = CTX.getNetwork().findAccount(accountId);
        assertNotNull(acc, "Account not found: " + accountId);
        assertEquals(expectedName, acc.getName());
    }

    @Then("the account {string} should have balance {double} EUR")
    public void theAccountShouldHaveBalance(String accountId, double expectedBalance) {
        Account acc = CTX.getNetwork().findAccount(accountId);
        assertNotNull(acc, "Account not found: " + accountId);
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }
}
