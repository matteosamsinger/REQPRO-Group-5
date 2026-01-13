package org.example.stepdefinitions.charger;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.*;

public class ChargerReadSteps {

    private Charger lastReadCharger;
    private Charger standaloneCharger;

    private ChargerType toChargerType(String typeText) {
        return ChargerType.valueOf(typeText.trim().toUpperCase());
    }

    @Given("a standalone charger with id {int} number {string} type {string} exists")
    public void aStandaloneChargerExists(int chargerId, String number, String type) {
        standaloneCharger = new Charger(chargerId, number, toChargerType(type));
        // KEIN location.setzen -> bleibt null
        // Status ist bei dir standardmäßig AVAILABLE (falls nicht: hier setzen)
        // standaloneCharger.setStatus(ChargerStatus.AVAILABLE);
    }

    @When("I read the charger {string} at location {string}")
    public void iReadTheChargerAtLocation(String number, String locationId) {
        CTX.clearLastException();
        lastReadCharger = null;

        try {
            Location location = CTX.getNetwork().readLocation(locationId);
            if (location == null) {
                throw new IllegalArgumentException("Location not found: " + locationId);
            }

            Charger charger = location.readChargerByNumber(number);
            if (charger == null) {
                throw new IllegalArgumentException("Charger not found at location " + locationId + ": " + number);
            }

            lastReadCharger = charger;

        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @When("I read the standalone charger {string}")
    public void iReadTheStandaloneCharger(String number) {
        CTX.clearLastException();
        lastReadCharger = null;

        try {
            if (standaloneCharger == null || !standaloneCharger.getNumber().equals(number)) {
                throw new IllegalArgumentException("Charger not found: " + number);
            }
            lastReadCharger = standaloneCharger;
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("I should see charger id {int} number {string} type {string} at location {string}")
    public void iShouldSeeChargerAtLocation(int expectedId, String expectedNumber, String expectedType, String expectedLocationId) {
        assertNotNull(lastReadCharger, "No charger was read");
        assertEquals(expectedId, lastReadCharger.getChargerId());
        assertEquals(expectedNumber, lastReadCharger.getNumber());
        assertEquals(expectedType.trim().toUpperCase(), lastReadCharger.getType().name());

        assertNotNull(lastReadCharger.getLocation(), "Expected charger to have a location");
        assertEquals(expectedLocationId, lastReadCharger.getLocation().readId());
    }

    @Then("I should see charger id {int} number {string} type {string} with no location")
    public void iShouldSeeChargerWithNoLocation(int expectedId, String expectedNumber, String expectedType) {
        assertNotNull(lastReadCharger, "No charger was read");
        assertEquals(expectedId, lastReadCharger.getChargerId());
        assertEquals(expectedNumber, lastReadCharger.getNumber());
        assertEquals(expectedType.trim().toUpperCase(), lastReadCharger.getType().name());

        assertNull(lastReadCharger.getLocation(), "Expected charger to have no location assigned");
    }

    @Then("the read charger should have status {string}")
    public void theReadChargerShouldHaveStatus(String expectedStatus) {
        assertNotNull(lastReadCharger, "No charger was read");
        ChargerStatus status = ChargerStatus.valueOf(expectedStatus.trim().toUpperCase());
        assertEquals(status, lastReadCharger.getStatus());
    }
}

