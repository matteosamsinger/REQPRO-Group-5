package org.example.steps.AccountSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.example.entities.Account;
import org.example.steps.support.ScenarioContext;

public class AccountCreateSteps {

    private final ScenarioContext ctx;

    public AccountCreateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @When("I register a client with id {string} name {string} and email {string}")
    public void iRegisterAClientWithIdNameAndEmail(String clientId, String name, String email) {
        Account account = new Account(clientId, name, email);
        ctx.network.createAccount(account);
    }

    @Given("a client with id {string} name {string} and email {string} and an empty account exists")
    public void aClientWithIdNameAndEmailAndAnEmptyAccountExists(String clientId, String name, String email) {
        Account account = new Account(clientId, name, email);
        ctx.network.createAccount(account);
    }

    @And("a client with id {string} name {string} and an account with balance {int} EUR exists")
    public void aClientWithIdNameAndAnAccountWithBalanceEURExists(String clientId, String name, int balance) {
        Account account = new Account(clientId, name, "dummy@example.com");

        if (balance > 0) {
            account.topUp(balance);
        } else if (balance < 0) {
            account.debit(-balance);
        }

        ctx.network.createAccount(account);
    }
}

