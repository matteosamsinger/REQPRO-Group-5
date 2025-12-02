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

    private Charger findChargerByNumber(String chargerNumber) {
        for (Location loc : network.getAllLocations()) {
            for (Charger charger : loc.getChargers()) {
                if (charger.getNumber().equals(chargerNumber)) {
                    return charger;
                }
            }
        }
        return null;
    }

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


    @When("I start a charging session for client {string} at charger {string} at {string}")
    public void iStartAChargingSessionForClientAtChargerAt(String clientId, String chargerNumber, String startTimeText) {
        currentClient = network.findClient(clientId);
        assertNotNull(currentClient, "Client not found: " + clientId);

        currentCharger = findChargerByNumber(chargerNumber);
        assertNotNull(currentCharger, "Charger not found: " + chargerNumber);

        java.time.LocalDateTime startTime = java.time.LocalDateTime.parse(startTimeText);

        currentSession = new ChargingSession(1, currentClient, currentCharger, startTime);

        // Charger ist jetzt im Status "CHARGING"
        currentCharger.setStatus("CHARGING");
    }


    @And("I stop the charging session at {string} with energy {double} kWh and pricePerKWh {double} EUR")
    public void iStopTheChargingSessionAtWithEnergyKWhAndPricePerKWhEur(String endTimeText, double energyKWh, double pricePerKWh) {
        assertNotNull(currentSession, "No current charging session");

        java.time.LocalDateTime endTime = java.time.LocalDateTime.parse(endTimeText);

        // Session beenden und Preis berechnen
        currentSession.stop(endTime, energyKWh, pricePerKWh);

        // Betrag vom Account abbuchen
        Account account = currentClient.getAccount();
        account.debit(currentSession.getTotalPrice());

        // Charger wieder verfügbar
        currentCharger.setStatus("AVAILABLE");
    }


    @Then("the charging session should have a duration of {int} minutes")
    public void theChargingSessionShouldHaveADurationOfMinutes(int expectedMinutes) {
        assertNotNull(currentSession, "No current charging session");
        assertEquals(expectedMinutes, currentSession.getDurationMinutes());
    }


    @And("the total price of the charging session should be {double} EUR")
    public void theTotalPriceOfTheChargingSessionShouldBeEur(double expectedPrice) {
        assertNotNull(currentSession, "No current charging session");
        assertEquals(expectedPrice, currentSession.getTotalPrice(), 0.0001);
    }


    @And("the account balance of client {string} should be {double} EUR")
    public void theAccountBalanceOfClientShouldBeEur(String clientId, double expectedBalance) {
        Client client = network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Account for client not found: " + clientId);

        assertEquals(expectedBalance, account.getBalance(), 0.0001);
    }


    @And("the charger {string} at location {string} should be available again")
    public void theChargerAtLocationShouldBeAvailableAgain(String chargerNumber, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = null;
        for (Charger c : location.getChargers()) {
            if (c.getNumber().equals(chargerNumber)) {
                charger = c;
                break;
            }
        }
        assertNotNull(charger, "Charger not found at location: " + chargerNumber);

        assertTrue(charger.isAvailable(), "Charger is not available");
    }

    @Given("charger {string} at location {string} is currently charging")
    public void chargerAtLocationIsCurrentlyCharging(String chargerNumber, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = null;
        for (Charger c : location.getChargers()) {
            if (c.getNumber().equals(chargerNumber)) {
                charger = c;
                break;
            }
        }
        assertNotNull(charger, "Charger not found: " + chargerNumber);

        charger.setStatus("CHARGING");
    }

    @And("charger {string} at location {string} is available")
    public void chargerAtLocationIsAvailable(String chargerNumber, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = null;
        for (Charger c : location.getChargers()) {
            if (c.getNumber().equals(chargerNumber)) {
                charger = c;
                break;
            }
        }
        assertNotNull(charger, "Charger not found: " + chargerNumber);

        charger.setStatus("AVAILABLE");
    }

    @When("I request the network status")
    public void iRequestTheNetworkStatus() {
        lastNetworkStatus = network.getNetworkStatus();
    }

    @Then("I see {int} charger status entries")
    public void iSeeChargerStatusEntries(int expectedCount) {
        assertNotNull(lastNetworkStatus, "Network status was not requested yet");
        assertEquals(expectedCount, lastNetworkStatus.size());
    }

    @And("one entry for charger {string} has status {string}")
    public void oneEntryForChargerHasStatus(String chargerNumber, String expectedStatus) {
        assertNotNull(lastNetworkStatus, "Network status was not requested yet");

        boolean found = false;
        for (Charger c : lastNetworkStatus) {
            if (c.getNumber().equals(chargerNumber)
                    && expectedStatus.equals(c.getStatus())) {
                found = true;
                break;
            }
        }

        assertTrue(found,
                "No charger " + chargerNumber + " with status " + expectedStatus + " found in network status");
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


    @And("I add a charging transaction of {int} EUR for client {string}")
    public void iAddAChargingTransactionOfEURForClient(int amount, String clientId) {
        Client client = network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Client has no account: " + clientId);

        int nextId = account.getTransactions().size() + 1;
        Transaction tx = new Transaction(nextId, nextId, amount);

        account.debit(amount);
        account.addTransaction(tx);
    }


    @And("I request the invoice status for client {string}")
    public void iRequestTheInvoiceStatusForClient(String clientId) {
        Client client = network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);
        // Nichts weiter nötig – ausgewertet wird im Then-Step
    }


    @Then("the invoice for client {string} should contain {int} top-ups and {int} charging transaction")
    public void theInvoiceForClientShouldContainTopUpsAndChargingTransaction(String clientId, int expectedTopUps, int expectedTransactions) {
        Client client = network.findClient(clientId);
        assertNotNull(client, "Client not found: " + clientId);

        Account account = client.getAccount();
        assertNotNull(account, "Client has no account: " + clientId);

        assertEquals(expectedTopUps, account.getTopUps().size());
        assertEquals(expectedTransactions, account.getTransactions().size());
    }


    @When("I set an energy tariff at location {string} with AC price per kWh {double} EUR and DC price per kWh {double} EUR")
    public void iSetAnEnergyTariffAtLocationWithACPricePerKWhAndDCPricePerKWh(String locationId, double acPrice, double dcPrice) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Tariff tariff = new Tariff(
                1,                // tariffId (hier einfach 1, reicht für MVP)
                LocalDate.now(),  // validFrom
                acPrice,          // pricePerKWhAC
                0.0,              // pricePerMinuteAC
                dcPrice,          // pricePerKWhDC
                0.0,              // pricePerMinuteDC
                location          // location
        );

        network.setEnergyTariffForLocation(locationId, tariff);
    }
    @Then("the energy tariff at location {string} should have AC price per kWh {double} EUR and DC price per kWh {double} EUR")
    public void theEnergyTariffAtLocationShouldHaveACPricePerKWhAndDCPricePerKWh(String locationId, double expectedAc, double expectedDc) {
        Tariff tariff = network.getEnergyTariffForLocation(locationId);
        assertNotNull(tariff, "No energy tariff set for location " + locationId);

        assertEquals(expectedAc, tariff.getPricePerKWhAC(), 0.0001);
        assertEquals(expectedDc, tariff.getPricePerKWhDC(), 0.0001);
    }
}

