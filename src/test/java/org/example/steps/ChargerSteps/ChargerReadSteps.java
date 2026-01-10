package org.example.steps.ChargerSteps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.Charger;
import org.example.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargerReadSteps {
    private final ScenarioContext ctx;

    @Then("the location with id {string} should have {int} charger")
    public void theLocationWithIdShouldHaveCharger(String locationId, int expectedCount) {
        Location location = ctx.network.findLocation(locationId);
        assertNotNull(location);
        assertEquals(expectedCount, location.getChargers().size());
    }

    @Then("the first charger at location {string} should have type {string}")
    public void theFirstChargerAtLocationShouldHaveType(String locationId, String expectedType) {
        Location location = ctx.network.findLocation(locationId);
        assertNotNull(location);
        assertFalse(location.getChargers().isEmpty(), "No chargers found at location " + locationId);

        Charger first = location.getChargers().get(0);
        assertEquals(expectedType, first.getType());
    }



    @When("I look up the charger with number {string} at location {string}")
    public void iLookUpTheChargerWithNumberAtLocation(String number, String locationId) {
        Location location = ctx.network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        ctx.lookedUpCharger = location.findChargerByNumber(number);
    }

    @Then("I see the charger type {string} and max power {int} kW")
    public void iSeeTheChargerTypeAndMaxPowerKW(String expectedType, int expectedMaxPower) {
        assertNotNull(ctx.lookedUpCharger, "No charger was looked up");
        assertEquals(expectedType, ctx.lookedUpCharger.getType());
        assertEquals(expectedMaxPower, ctx.lookedUpCharger.getMaxPowerKw());
    }

    public ChargerReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
