package org.example.stepdefinitions.charger;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.enums.ChargerStatus;
import org.example.enums.ChargerType;
import org.example.stepdefinitions.common.CommonErrorSteps;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargerDeleteSteps {

    @When("I delete the charger with number {string} at location {string}")
    public void iDeleteTheChargerWithNumberAtLocation(String number, String locationId) {
        CTX.clearLastException();
        try {
            CTX.getNetwork().deleteChargerFromLocation(locationId, number);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @When("I try to delete the charger with number {string} at location {string}")
    public void iTryToDeleteTheChargerWithNumberAtLocation(String number, String locationId) {
        iDeleteTheChargerWithNumberAtLocation(number, locationId);
    }

    @And("charger {string} at location {string} is currently charging")
    public void chargerAtLocationIsCurrentlyCharging(String number, String locationId) {
        Location location = CTX.getNetwork().readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Charger charger = location.readChargerByNumber(number);
        assertNotNull(charger, "Charger not found: " + number);

        charger.setStatus(ChargerStatus.IN_USE);
    }
}
