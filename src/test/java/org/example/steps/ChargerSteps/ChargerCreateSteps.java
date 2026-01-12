package org.example.steps.ChargerSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.enums.ChargerType;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargerCreateSteps {
    private final ScenarioContext ctx;

    public ChargerCreateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Given("a charger with number {string} type {string} and max power {int} kW at location {string} exists")
    public void aChargerWithNumberTypeAndMaxPowerKWAtLocationExists(String number, String type, int maxPower, String locationId) {
        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        int chargerId = location.readChargers().size() + 1;

        ChargerType chargerType = ChargerType.valueOf(type.trim().toUpperCase());
        Charger charger = new Charger(chargerId, number, chargerType);
        charger.setLocation(location);


        ctx.network.addChargerToLocation(locationId, charger);
    }

    @When("I add a charger with number {string} type {string} and max power {int} kW to location {string}")
    public void iAddAChargerWithNumberTypeAndMaxPowerToLocation(String number, String type, int maxPower, String locationId) {
        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        int chargerId = location.readChargers().size() + 1;

        ChargerType chargerType = ChargerType.valueOf(type.trim().toUpperCase());
        Charger charger = new Charger(chargerId, number, chargerType);
        charger.setLocation(location);


        ctx.network.addChargerToLocation(locationId, charger);
    }
}

