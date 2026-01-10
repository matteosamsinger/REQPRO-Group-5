package org.example.steps.LocationSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.ElectricChargingStationNetwork;
import org.example.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationCreateSteps {
    private final ScenarioContext ctx;

    @When("I create a location with id {string} name {string} and address {string}")
    public void iCreateALocationWithIdNameAndAddress(String id, String name, String address) {
        Location location = new Location(id, name, address);
        ctx.network.addLocation(location);
    }

    @Then("there should be a location with id {string} and name {string}")
    public void thereShouldBeALocationWithIdAndName(String id, String expectedName) {
        Location found = ctx.network.findLocation(id);
        assertNotNull(found);
        assertEquals(expectedName, found.getName());
    }

    public LocationCreateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
