package org.example.stepdefinitions.location;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocationDeleteSteps {

    @When("I delete the location {string}")
    public void iDeleteTheLocation(String locationId) {
        CTX.clearLastException();
        try {
            CTX.getNetwork().deleteLocation(locationId);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("there should be no location with id {string}")
    public void thereShouldBeNoLocationWithId(String id) {
        Location found = CTX.getNetwork().readLocation(id);
        assertNull(found, "Location should be deleted but was found: " + id);
    }
}
