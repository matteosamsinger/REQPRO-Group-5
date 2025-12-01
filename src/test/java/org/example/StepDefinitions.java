package org.example;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;


import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private ElectricChargingStationNetwork network;
    private Location lookedUpLocation;
    private Charger lookedUpCharger;
    private Client lookedUpClient;

    @Given("an empty charging network")
    public void anEmptyChargingNetwork() {
        network = new ElectricChargingStationNetwork();
    }

    @When("I create a location with id {string} name {string} and address {string}")
    public void iCreateALocationWithIdNameAndAddress(String id, String name, String address) {
        Location location = new Location(id, name, address);
        network.addLocation(location);
    }

    @Then("there should be a location with id {string} and name {string}")
    public void thereShouldBeALocationWithIdAndName(String id, String expectedName) {
        Location found = network.findLocation(id);
        assertNotNull(found);
        assertEquals(expectedName, found.getName());
    }

    @Given("a location with id {string} name {string} and address {string} exists")
    public void aLocationWithIdNameAndAddressExists(String id, String name, String address) {
        Location location = new Location(id, name, address);
        network.addLocation(location);
    }

    @When("I look up the location with id {string}")
    public void iLookUpTheLocationWithId(String id) {
        lookedUpLocation = network.findLocation(id);
    }

    @Then("I see the location name {string} and address {string}")
    public void iSeeTheLocationNameAndAddress(String expectedName, String expectedAddress) {
        assertNotNull(lookedUpLocation);
        assertEquals(expectedName, lookedUpLocation.getName());
        assertEquals(expectedAddress, lookedUpLocation.getAddress());
    }

    @When("I change the name of the location with id {string} to {string}")
    public void iChangeTheNameOfTheLocationWithIdTo(String id, String newName) {
        Location location = network.findLocation(id);
        location.setName(newName);
    }

    @Then("the location with id {string} should have name {string}")
    public void theLocationWithIdShouldHaveName(String id, String expectedName) {
        Location location = network.findLocation(id);
        assertNotNull(location);
        assertEquals(expectedName, location.getName());
    }

    @When("I delete the location with id {string}")
    public void iDeleteTheLocationWithId(String id) {
        network.deleteLocation(id);
    }

    @Then("there should be no location with id {string}")
    public void thereShouldBeNoLocationWithId(String id) {
        Location location = network.findLocation(id);
        assertNull(location);
    }

    @When("I add a charger with number {string} type {string} and max power {int} kW to location {string}")
    public void iAddAChargerWithNumberTypeAndMaxPowerToLocation(String number, String type, int maxPower, String locationId) {
        // einfache Id: Anzahl der Charger + 1 oder einfach 1 (für dieses Scenario egal)
        Location location = network.findLocation(locationId);
        int chargerId = location.getChargers().size() + 1;

        Charger charger = new Charger(chargerId, number, type, maxPower, location);
        network.addChargerToLocation(locationId, charger);
    }


    @Then("the location with id {string} should have {int} charger")
    public void theLocationWithIdShouldHaveCharger(String locationId, int expectedCount) {
        Location location = network.findLocation(locationId);
        assertNotNull(location);
        assertEquals(expectedCount, location.getChargers().size());
    }

    @Then("the first charger at location {string} should have type {string}")
    public void theFirstChargerAtLocationShouldHaveType(String locationId, String expectedType) {
        Location location = network.findLocation(locationId);
        assertNotNull(location);
        assertFalse(location.getChargers().isEmpty(), "No chargers found at location " + locationId);

        Charger first = location.getChargers().get(0);
        assertEquals(expectedType, first.getType());
    }

    @Given("a charger with number {string} type {string} and max power {int} kW at location {string} exists")
    public void aChargerWithNumberTypeAndMaxPowerKWAtLocationExists(String number, String type, int maxPower, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        int chargerId = location.getChargers().size() + 1;
        Charger charger = new Charger(chargerId, number, type, maxPower, location);
        network.addChargerToLocation(locationId, charger);
    }

    @When("I look up the charger with number {string} at location {string}")
    public void iLookUpTheChargerWithNumberAtLocation(String number, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        lookedUpCharger = location.findChargerByNumber(number);
    }

    @Then("I see the charger type {string} and max power {int} kW")
    public void iSeeTheChargerTypeAndMaxPowerKW(String expectedType, int expectedMaxPower) {
        assertNotNull(lookedUpCharger, "No charger was looked up");
        assertEquals(expectedType, lookedUpCharger.getType());
        assertEquals(expectedMaxPower, lookedUpCharger.getMaxPowerKw());
    }

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

    @When("I delete the charger with number {string} at location {string}")
    public void iDeleteTheChargerWithNumberAtLocation(String number, String locationId) {
        network.removeChargerFromLocation(locationId, number);
    }
}

