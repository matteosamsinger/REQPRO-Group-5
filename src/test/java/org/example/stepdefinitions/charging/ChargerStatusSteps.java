package org.example.stepdefinitions.charging;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.enums.ChargerStatus;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargerStatusSteps {

    @Then("charger {string} at location {string} should have status {string}")
    public void chargerAtLocationShouldHaveStatus(String number, String locationId, String expectedStatus) {
        Location loc = CTX.getNetwork().readLocation(locationId);
        assertNotNull(loc, "Location not found: " + locationId);

        Charger c = loc.readChargerByNumber(number);
        assertNotNull(c, "Charger not found: " + number + " at " + locationId);

        ChargerStatus status = ChargerStatus.valueOf(expectedStatus.trim().toUpperCase());
        assertEquals(status, c.getStatus());
    }


    @And("charger {string} at location {string} is out of order")
    public void chargerAtLocationIsOutOfOrder(String number, String locationId) {
        Location location = CTX.getNetwork().readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(number);
        assertNotNull(charger, "Charger not found: " + number);

        charger.setStatus(ChargerStatus.OUT_OF_ORDER);
    }

}
