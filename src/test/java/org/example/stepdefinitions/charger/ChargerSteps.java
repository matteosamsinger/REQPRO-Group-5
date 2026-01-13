package org.example.stepdefinitions.charger;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ChargerSteps {

    private ChargerType toChargerType(String typeText) {
        return ChargerType.valueOf(typeText.trim().toUpperCase());
    }

    @When("I add a charger with number {string} type {string} to location {string}")
    public void iAddAChargerToLocation(String number, String type, String locationId) {
        CTX.clearLastException();
        try {
            Location location = CTX.getNetwork().readLocation(locationId);
            assertNotNull(location, "Location not found: " + locationId);

            int chargerId = location.readChargers().size() + 1;
            Charger charger = new Charger(chargerId, number, toChargerType(type));

            CTX.getNetwork().addChargerToLocation(locationId, charger);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Given("a charger with number {string} type {string} at location {string} exists")
    public void aChargerExistsAtLocation(String number, String type, String locationId) {
        iAddAChargerToLocation(number, type, locationId);

        if (CTX.getLastException() != null) {
            fail("Could not create charger: " + CTX.getLastException().getMessage());
        }

        Location location = CTX.getNetwork().readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);
        assertNotNull(location.readChargerByNumber(number), "Charger not found after creation: " + number);
    }

    @Then("the location {string} should have {int} charger")
    public void theLocationShouldHaveChargers(String locationId, int expectedCount) {
        Location location = CTX.getNetwork().readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        assertEquals(expectedCount, location.readChargers().size());
    }

    @Then("the charger {string} at location {string} should have type {string}")
    public void theChargerAtLocationShouldHaveType(String chargerNumber, String locationId, String expectedType) {
        Location location = CTX.getNetwork().readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(chargerNumber);
        assertNotNull(charger, "Charger not found: " + chargerNumber + " at " + locationId);

        assertEquals(expectedType.trim().toUpperCase(), charger.getType().name());
    }


}
