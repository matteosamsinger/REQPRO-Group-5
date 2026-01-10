package org.example;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class StepDefinitions {


    private ElectricChargingStationNetwork network;
    private Location lookedUpLocation;
    private Charger lookedUpCharger;
    private Client lookedUpClient;
    private ChargingSession currentSession;
    private Client currentClient;
    private Charger currentCharger;
    private java.util.List<Charger> lastNetworkStatus;


    @When("I register a client with id {string} name {string} and email {string}")
    public void iRegisterAClientWithIdNameAndEmail(String clientId, String name, String email) {
        Account account = new Account();
        Client client = new Client(clientId, name, email, account);
        network.registerClient(client);
    }

    @Then("there should be a client with id {string} and name {string}")
    public void thereShouldBeAClientWithIdAndName(String clientId, String expectedName) {
        Client client = network.findClient(clientId);
        assertNotNull(client);
        assertEquals(expectedName, client.getName());
    }

    @Then("the client {string} should have an account with balance 0 EUR")
    public void theClientShouldHaveAnAccountWithBalance0Eur(String clientId) {
        Client client = network.findClient(clientId);
        assertNotNull(client);
        assertNotNull(client.getAccount());
        assertEquals(0.0, client.getAccount().getBalance(), 0.0001);
    }

    @Given("a client with id {string} name {string} and email {string} and an empty account exists")
    public void aClientWithIdNameAndEmailAndAnEmptyAccountExists(String clientId, String name, String email) {
        Account account = new Account();
        Client client = new Client(clientId, name, email, account);
        network.registerClient(client);
    }

    @When("I top up the account of client {string} by {double} EUR")
    public void iTopUpTheAccountOfClientByEur(String clientId, double amount) {
        Client client = network.findClient(clientId);
        assertNotNull(client);
        client.getAccount().topUp(amount);
    }

    @Then("the account of client {string} should have a balance of {double} EUR")
    public void theAccountOfClientShouldHaveABalanceOfEur(String clientId, double expectedBalance) {
        Client client = network.findClient(clientId);
        assertNotNull(client);
        assertNotNull(client.getAccount());
        assertEquals(expectedBalance, client.getAccount().getBalance(), 0.0001);
    }


    @And("the account of client {string} has a balance of {double} EUR")
    public void theAccountOfClientHasABalanceOfEur(String clientId, double amount) {
        Client client = network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Account for client not found: " + clientId);

        // In unserem Background haben wir "empty account" → Balance sollte 0 sein:
        assertEquals(0.0, account.getBalance(), 0.0001);

        account.topUp(amount);
    }


    @And("a client with id {string} name {string} and an account with balance {int} EUR exists")
    public void aClientWithIdNameAndAnAccountWithBalanceEURExists(String clientId, String name, int balance) {
        Account account = new Account();

        if (balance > 0) {
            account.topUp(balance);
        } else if (balance < 0) {
            account.debit(-balance);
        }

        Client client = new Client(clientId, name, "dummy@example.com", account);

        network.addClient(client);
    }

}










