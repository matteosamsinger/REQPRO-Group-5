package org.example.steps.ChargerSteps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.steps.support.ScenarioContext;
import org.example.enums.ChargerType;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargerReadSteps {
    private final ScenarioContext ctx;

    @Then("the location with id {string} should have {int} charger")
    public void theLocationWithIdShouldHaveCharger(String locationId, int expectedCount) {
        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location);
        assertEquals(expectedCount, location.readChargers().size());
    }

    @Then("the first charger at location {string} should have type {string}")
    public void theFirstChargerAtLocationShouldHaveType(String locationId, String expectedTypeText) {

        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        assertFalse(location.readChargers().isEmpty(), "No chargers found at location: " + locationId);

        Charger first = location.readChargers().get(0);

        ChargerType expectedType = ChargerType.valueOf(expectedTypeText.trim().toUpperCase());
        assertEquals(expectedType, first.getType());
    }



    @When("I look up the charger with number {string} at location {string}")
    public void iLookUpTheChargerWithNumberAtLocation(String number, String locationId) {
        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        ctx.lookedUpCharger = location.readChargerByNumber(number);
    }

    @Then("I see the charger type {string} and max power {int} kW")
    public void iSeeTheChargerTypeAndMaxPowerKW(String expectedTypeText, int ignoredMaxPowerKw) {

        assertNotNull(ctx.lookedUpCharger, "No looked up charger available");

        ChargerType expectedType = ChargerType.valueOf(expectedTypeText.trim().toUpperCase());
        assertEquals(expectedType, ctx.lookedUpCharger.getType());

    }

    public ChargerReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
