package org.example.stepdefinitions.charger;

import io.cucumber.java.en.When;
import org.example.enums.ChargerType;

import static org.example.stepdefinitions.common.CommonSteps.CTX;

public class ChargerUpdateSteps {


    private ChargerType toChargerType(String typeText) {
        return ChargerType.valueOf(typeText.trim().toUpperCase());
    }

    @When("I update the charger {string} at location {string} type to {string}")
    public void iUpdateTheChargerType(String chargerNumber, String locationId, String newType) {
        CTX.clearLastException();
        try {
            CTX.getNetwork().updateChargerType(locationId, chargerNumber, toChargerType(newType));
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }
}

