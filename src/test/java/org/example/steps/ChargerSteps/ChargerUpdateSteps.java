package org.example.steps.ChargerSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.example.Charger;
import org.example.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChargerUpdateSteps {
    private final ScenarioContext ctx;

    @Given("charger {string} at location {string} is currently charging")
    public void chargerAtLocationIsCurrentlyCharging(String chargerNumber, String locationId) {
        Location location = ctx.network.findLocation(locationId);
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
        Location location = ctx.network.findLocation(locationId);
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

    @And("the charger {string} at location {string} should be available again")
    public void theChargerAtLocationShouldBeAvailableAgain(String chargerNumber, String locationId) {
        Location location = ctx.network.findLocation(locationId);
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

    public ChargerUpdateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}