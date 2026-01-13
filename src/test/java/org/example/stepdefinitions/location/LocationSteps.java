package org.example.stepdefinitions.location;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationSteps {

    @Given("a location with id {string} name {string} and address {string} exists")
    public void aLocationExists(String id, String name, String address) {
        Location location = new Location(id, name, address);
        CTX.getNetwork().createLocation(location);
    }

    @When("I create a location with id {string} name {string} and address {string}")
    public void iCreateALocation(String id, String name, String address) {
        CTX.clearLastException();
        try {
            Location location = new Location(id, name, address);
            CTX.getNetwork().createLocation(location);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("there should be a location with id {string} and name {string}")
    public void thereShouldBeALocation(String id, String expectedName) {
        Location found = CTX.getNetwork().readLocation(id);
        assertNotNull(found, "Location not found: " + id);

        // je nach deiner Location API:
        // falls du readName() hast:
        assertEquals(expectedName, found.readName());
        // falls du getName() hast, ersetze durch:
        // assertEquals(expectedName, found.getName());
    }

    @Then("I see the location name {string} and address {string} for location {string}")
    public void iSeeNameAndAddressForLocation(String expectedName, String expectedAddress, String id) {
        Location found = CTX.getNetwork().readLocation(id);
        assertNotNull(found, "Location not found: " + id);

        assertEquals(expectedName, found.readName());
        assertEquals(expectedAddress, found.readAddress());
    }
}
