package org.example.steps.LocationSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;

public class LocationDeleteSteps {
    private final ScenarioContext ctx;

    @When("I delete the location with id {string}")
    public void iDeleteTheLocationWithId(String id) {
        ctx.network.deleteLocation(id);
    }

    @Then("there should be no location with id {string}")
    public void thereShouldBeNoLocationWithId(String id) {
        Location location = ctx.network.readLocation(id);
        assertNull(location);
    }

    public LocationDeleteSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
