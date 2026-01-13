package org.example.stepdefinitions.location;

import io.cucumber.java.en.When;

import static org.example.stepdefinitions.common.CommonSteps.CTX;

public class LocationUpdateSteps {

    @When("I update the location {string} name to {string}")
    public void iUpdateTheLocationNameTo(String locationId, String newName) {
        CTX.clearLastException();
        try {
            // Facade-Methode (du hast die schon):
            CTX.getNetwork().updateLocationName(locationId, newName);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }
}
