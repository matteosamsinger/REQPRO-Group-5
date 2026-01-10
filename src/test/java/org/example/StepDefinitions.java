package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.app.ElectricChargingStationNetwork;
import org.example.entities.*;
import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private ElectricChargingStationNetwork network;
    private Location lookedUpLocation;
    private Charger lookedUpCharger;

    // Client -> Account
    private Account lookedUpAccount;
    private Account currentAccount;

    private ChargingSession currentSession;
    private Charger currentCharger;
    private List<Charger> lastNetworkStatus;

    private Charger findChargerByNumber(String chargerNumber) {
        for (Location loc : network.readAllLocations()) {
            for (Charger charger : loc.readChargers()) {
                if (charger.getNumber().equals(chargerNumber)) {
                    return charger;
                }
            }
        }
        return null;
    }

    private ChargerType toChargerType(String typeText) {
        if (typeText == null) throw new IllegalArgumentException("typeText is null");
        return ChargerType.valueOf(typeText.trim().toUpperCase());
    }

    private ChargerStatus toChargerStatus(String statusText) {
        if (statusText == null) throw new IllegalArgumentException("statusText is null");
        String s = statusText.trim().toUpperCase();

        // Featurefile kann "CHARGING" sagen – im Code heißt es IN_USE
        if (s.equals("CHARGING")) return ChargerStatus.IN_USE;
        if (s.equals("IN_USE")) return ChargerStatus.IN_USE;

        if (s.equals("AVAILABLE")) return ChargerStatus.AVAILABLE;

        // optional: falls ihr solche Worte in Features habt
        if (s.equals("OUT_OF_ORDER") || s.equals("BROKEN")) return ChargerStatus.OUT_OF_ORDER;

        return ChargerStatus.valueOf(s);
    }

    // -------------------------
    // Network / Location CRUD
    // -------------------------

    @Given("an empty charging network")
    public void anEmptyChargingNetwork() {
        network = new ElectricChargingStationNetwork();
    }

    @When("I create a location with id {string} name {string} and address {string}")
    public void iCreateALocationWithIdNameAndAddress(String id, String name, String address) {
        Location location = new Location(id, name, address);
        network.createLocation(location);
    }

    @Then("there should be a location with id {string} and name {string}")
    public void thereShouldBeALocationWithIdAndName(String id, String expectedName) {
        Location found = network.findLocation(id);
        assertNotNull(found);
        assertEquals(expectedName, found.readName());
    }

    @Given("a location with id {string} name {string} and address {string} exists")
    public void aLocationWithIdNameAndAddressExists(String id, String name, String address) {
        Location location = new Location(id, name, address);
        network.createLocation(location);
    }

    @When("I look up the location with id {string}")
    public void iLookUpTheLocationWithId(String id) {
        lookedUpLocation = network.findLocation(id);
    }

    @Then("I see the location name {string} and address {string}")
    public void iSeeTheLocationNameAndAddress(String expectedName, String expectedAddress) {
        assertNotNull(lookedUpLocation);
        assertEquals(expectedName, lookedUpLocation.readName());
        assertEquals(expectedAddress, lookedUpLocation.readAddress());
    }

    @When("I change the name of the location with id {string} to {string}")
    public void iChangeTheNameOfTheLocationWithIdTo(String id, String newName) {
        Location location = network.findLocation(id);
        assertNotNull(location);
        location.updateName(newName);
    }

    @Then("the location with id {string} should have name {string}")
    public void theLocationWithIdShouldHaveName(String id, String expectedName) {
        Location location = network.findLocation(id);
        assertNotNull(location);
        assertEquals(expectedName, location.readName());
    }

    @When("I delete the location with id {string}")
    public void iDeleteTheLocationWithId(String id) {
        network.deleteLocation(id);
    }

    @Then("there should be no location with id {string}")
    public void thereShouldBeNoLocationWithId(String id) {
        assertNull(network.findLocation(id));
    }

    // -------------------------
    // Charger CRUD / Lookup
    // -------------------------

    @When("I add a charger with number {string} type {string} and max power {int} kW to location {string}")
    public void iAddAChargerWithNumberTypeAndMaxPowerToLocation(String number, String type, int maxPower, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        int chargerId = location.readChargers().size() + 1;
        Charger charger = new Charger(chargerId, number, toChargerType(type), maxPower, location);

        network.addChargerToLocation(locationId, charger);
    }

    @Then("the location with id {string} should have {int} charger")
    public void theLocationWithIdShouldHaveCharger(String locationId, int expectedCount) {
        Location location = network.findLocation(locationId);
        assertNotNull(location);
        assertEquals(expectedCount, location.readChargers().size());
    }

    @Then("the first charger at location {string} should have type {string}")
    public void theFirstChargerAtLocationShouldHaveType(String locationId, String expectedType) {
        Location location = network.findLocation(locationId);
        assertNotNull(location);
        assertFalse(location.readChargers().isEmpty(), "No chargers found at location " + locationId);

        Charger first = location.readChargers().get(0);
        assertEquals(expectedType.trim().toUpperCase(), first.getType().name());
    }

    @Given("a charger with number {string} type {string} and max power {int} kW at location {string} exists")
    public void aChargerWithNumberTypeAndMaxPowerKWAtLocationExists(String number, String type, int maxPower, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        int chargerId = location.readChargers().size() + 1;
        Charger charger = new Charger(chargerId, number, toChargerType(type), maxPower, location);

        network.addChargerToLocation(locationId, charger);
    }

    @When("I look up the charger with number {string} at location {string}")
    public void iLookUpTheChargerWithNumberAtLocation(String number, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        lookedUpCharger = location.readChargerByNumber(number);
    }

    @Then("I see the charger type {string} and max power {int} kW")
    public void iSeeTheChargerTypeAndMaxPowerKW(String expectedType, int expectedMaxPower) {
        assertNotNull(lookedUpCharger, "No charger was looked up");
        assertEquals(expectedType.trim().toUpperCase(), lookedUpCharger.getType().name());
        assertEquals(expectedMaxPower, lookedUpCharger.getMaxPowerKw());
    }

    @When("I delete the charger with number {string} at location {string}")
    public void iDeleteTheChargerWithNumberAtLocation(String number, String locationId) {
        network.deleteChargerFromLocation(locationId, number);
    }

    // -------------------------
    // Account (was Client)
    // -------------------------

    @When("I register a client with id {string} name {string} and email {string}")
    public void iRegisterAClientWithIdNameAndEmail(String clientId, String name, String email) {
        // Feature sagt "client", im Code ist es "account"
        Account account = new Account(clientId, name, email);
        network.createAccount(account);
    }

    @Then("there should be a client with id {string} and name {string}")
    public void thereShouldBeAClientWithIdAndName(String clientId, String expectedName) {
        Account acc = network.findAccount(clientId);
        assertNotNull(acc);
        assertEquals(expectedName, acc.getName());
    }

    @Then("the client {string} should have an account with balance 0 EUR")
    public void theClientShouldHaveAnAccountWithBalance0Eur(String clientId) {
        Account acc = network.findAccount(clientId);
        assertNotNull(acc);
        assertEquals(0.0, acc.getBalance(), 0.0001);
    }

    @Given("a client with id {string} name {string} and email {string} and an empty account exists")
    public void aClientWithIdNameAndEmailAndAnEmptyAccountExists(String clientId, String name, String email) {
        Account account = new Account(clientId, name, email);
        network.createAccount(account);
    }

    @When("I top up the account of client {string} by {double} EUR")
    public void iTopUpTheAccountOfClientByEur(String clientId, double amount) {
        Account acc = network.findAccount(clientId);
        assertNotNull(acc);
        acc.topUp(amount);
    }

    @Then("the account of client {string} should have a balance of {double} EUR")
    public void theAccountOfClientShouldHaveABalanceOfEur(String clientId, double expectedBalance) {
        Account acc = network.findAccount(clientId);
        assertNotNull(acc);
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }

    @And("the account of client {string} has a balance of {double} EUR")
    public void theAccountOfClientHasABalanceOfEur(String clientId, double amount) {
        Account acc = network.findAccount(clientId);
        assertNotNull(acc, "Account not found: " + clientId);

        // Im Background steht oft "empty account"
        // Wenn das bei euch nicht immer stimmt, kannst du diesen Assert entfernen.
        assertEquals(0.0, acc.getBalance(), 0.0001);

        acc.topUp(amount);
    }

    // -------------------------
    // Charging Session (start/stop via Network)
    // -------------------------

    @When("I start a charging session for client {string} at charger {string} at {string}")
    public void iStartAChargingSessionForClientAtChargerAt(String clientId, String chargerNumber, String startTimeText) {
        currentAccount = network.findAccount(clientId);
        assertNotNull(currentAccount, "Account not found: " + clientId);

        currentCharger = findChargerByNumber(chargerNumber);
        assertNotNull(currentCharger, "Charger not found: " + chargerNumber);

        LocalDateTime startTime = LocalDateTime.parse(startTimeText);

        // LocationId aus Charger ableiten
        String locationId = currentCharger.getLocation().readId();

        // Session über Facade starten (zieht Tarif zum Startzeitpunkt!)
        currentSession = network.startChargingSession(clientId, locationId, chargerNumber, startTime);
        assertNotNull(currentSession);
    }

    @And("I stop the charging session at {string} with energy {double} kWh and pricePerKWh {double} EUR")
    public void iStopTheChargingSessionAtWithEnergyKWhAndPricePerKWhEur(String endTimeText, double energyKWh, double pricePerKWh) {
        assertNotNull(currentSession, "No current charging session");

        LocalDateTime endTime = LocalDateTime.parse(endTimeText);

        // Im neuen Modell kommt pricePerKWh NICHT mehr in stop() rein,
        // weil die Preise beim Start aus dem Tarif "eingefroren" werden.
        // Der Parameter bleibt hier nur, damit eure bestehenden Featurefiles matchen.
        ChargingSession finished = network.stopChargingSession(currentSession.getSessionId(), endTime, energyKWh);
        currentSession = finished;

        assertNotNull(currentSession);
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
        Account acc = network.findAccount(clientId);
        assertNotNull(acc, "Account not found: " + clientId);
        assertEquals(expectedBalance, acc.getBalance(), 0.0001);
    }

    @And("the charger {string} at location {string} should be available again")
    public void theChargerAtLocationShouldBeAvailableAgain(String chargerNumber, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(chargerNumber);
        assertNotNull(charger, "Charger not found at location: " + chargerNumber);

        assertTrue(charger.isAvailable(), "Charger is not available");
    }

    @Given("charger {string} at location {string} is currently charging")
    public void chargerAtLocationIsCurrentlyCharging(String chargerNumber, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(chargerNumber);
        assertNotNull(charger, "Charger not found: " + chargerNumber);

        charger.setStatus(ChargerStatus.IN_USE);
    }

    @And("charger {string} at location {string} is available")
    public void chargerAtLocationIsAvailable(String chargerNumber, String locationId) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(chargerNumber);
        assertNotNull(charger, "Charger not found: " + chargerNumber);

        charger.setStatus(ChargerStatus.AVAILABLE);
    }

    // -------------------------
    // Network status
    // -------------------------

    @When("I request the network status")
    public void iRequestTheNetworkStatus() {
        lastNetworkStatus = network.readNetworkStatus();
    }

    @Then("I see {int} charger status entries")
    public void iSeeChargerStatusEntries(int expectedCount) {
        assertNotNull(lastNetworkStatus, "Network status was not requested yet");
        assertEquals(expectedCount, lastNetworkStatus.size());
    }

    @And("one entry for charger {string} has status {string}")
    public void oneEntryForChargerHasStatus(String chargerNumber, String expectedStatus) {
        assertNotNull(lastNetworkStatus, "Network status was not requested yet");

        ChargerStatus exp = toChargerStatus(expectedStatus);

        boolean found = false;
        for (Charger c : lastNetworkStatus) {
            if (c.getNumber().equals(chargerNumber) && c.getStatus() == exp) {
                found = true;
                break;
            }
        }

        assertTrue(found, "No charger " + chargerNumber + " with status " + expectedStatus + " found in network status");
    }

    // -------------------------
    // Invoice-ish (TopUps / Transactions)
    // -------------------------

    @And("a client with id {string} name {string} and an account with balance {int} EUR exists")
    public void aClientWithIdNameAndAnAccountWithBalanceEURExists(String clientId, String name, int balance) {
        Account account = new Account(clientId, name, "dummy@example.com");

        if (balance > 0) {
            account.topUp(balance);
        } else if (balance < 0) {
            account.debit(-balance);
        }

        network.createAccount(account);
    }

    @And("I add a charging transaction of {int} EUR for client {string}")
    public void iAddAChargingTransactionOfEURForClient(int amount, String clientId) {
        Account account = network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);

        int nextId = account.getTransactions().size() + 1;
        Transaction tx = new Transaction(nextId, nextId, amount);

        account.debit(amount);
        account.addTransaction(tx);
    }

    @And("I request the invoice status for client {string}")
    public void iRequestTheInvoiceStatusForClient(String clientId) {
        Account account = network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);
        // Nichts weiter nötig – ausgewertet wird im Then-Step
    }

    @Then("the invoice for client {string} should contain {int} top-ups and {int} charging transaction")
    public void theInvoiceForClientShouldContainTopUpsAndChargingTransaction(String clientId, int expectedTopUps, int expectedTransactions) {
        Account account = network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);

        assertEquals(expectedTopUps, account.getTopUps().size());
        assertEquals(expectedTransactions, account.getTransactions().size());
    }

    // -------------------------
    // Tariff Steps (updated to LocalDateTime + new Tariff API)
    // -------------------------

    @When("I set an energy tariff at location {string} with AC price per kWh {double} EUR and DC price per kWh {double} EUR")
    public void iSetAnEnergyTariffAtLocationWithACPricePerKWhAndDCPricePerKWh(String locationId, double acPrice, double dcPrice) {
        Location location = network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        // wichtig: validFrom muss so früh sein, dass es auch für beliebige Test-Zeitpunkte gilt
        LocalDateTime validFrom = LocalDateTime.of(2000, 1, 1, 0, 0);

        Tariff tariff = new Tariff(
                1,
                validFrom,
                acPrice, 0.0,   // AC: €/kWh, €/minute
                dcPrice, 0.0    // DC: €/kWh, €/minute
        );

        network.createTariffForLocation(locationId, tariff);
    }

    @Then("the energy tariff at location {string} should have AC price per kWh {double} EUR and DC price per kWh {double} EUR")
    public void theEnergyTariffAtLocationShouldHaveACPricePerKWhAndDCPricePerKWh(String locationId, double expectedAc, double expectedDc) {
        Tariff tariff = network.readCurrentTariffForLocation(locationId);
        assertNotNull(tariff, "No energy tariff set for location " + locationId);

        assertEquals(expectedAc, tariff.getPricePerKWh(ChargerType.AC), 0.0001);
        assertEquals(expectedDc, tariff.getPricePerKWh(ChargerType.DC), 0.0001);
    }
}