package org.example.steps.ChargerSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.example.Charger;
import org.example.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargerCreateSteps {
    private final ScenarioContext ctx;

    @Given("a charger with number {string} type {string} and max power {int} kW at location {string} exists")
    public void aChargerWithNumberTypeAndMaxPowerKWAtLocationExists(String number, String type, int maxPower, String locationId) {
        Location location = ctx.network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        int chargerId = location.getChargers().size() + 1;
        Charger charger = new Charger(chargerId, number, type, maxPower, location);
        ctx.network.addChargerToLocation(locationId, charger);
    }

    @When("I add a charger with number {string} type {string} and max power {int} kW to location {string}")
    public void iAddAChargerWithNumberTypeAndMaxPowerToLocation(String number, String type, int maxPower, String locationId) {
        // einfache Id: Anzahl der Charger + 1 oder einfach 1 (für dieses Scenario egal)
        Location location = ctx.network.findLocation(locationId);
        int chargerId = location.getChargers().size() + 1;

        Charger charger = new Charger(chargerId, number, type, maxPower, location);
        ctx.network.addChargerToLocation(locationId, charger);
    }

    public ChargerCreateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
