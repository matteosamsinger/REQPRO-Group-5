package org.example.steps.LocationSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationReadSteps {
    private final ScenarioContext ctx;

    @Given("a location with id {string} name {string} and address {string} exists")
    public void aLocationWithIdNameAndAddressExists(String id, String name, String address) {
        Location location = new Location(id, name, address);
        ctx.network.createLocation(location);
    }

    @When("I look up the location with id {string}")
    public void iLookUpTheLocationWithId(String id) {
        ctx.lookedUpLocation = ctx.network.readLocation(id);
    }

    @Then("I see the location name {string} and address {string}")
    public void iSeeTheLocationNameAndAddress(String expectedName, String expectedAddress) {
        assertNotNull(ctx.lookedUpLocation);
        assertEquals(expectedName, ctx.lookedUpLocation.readName());
        assertEquals(expectedAddress, ctx.lookedUpLocation.readAddress());
    }

    public LocationReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}