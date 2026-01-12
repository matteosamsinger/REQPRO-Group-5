package org.example.steps.LocationSteps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationUpdateSteps {
    private final ScenarioContext ctx;

    @When("I change the name of the location with id {string} to {string}")
    public void iChangeTheNameOfTheLocationWithIdTo(String id, String newName) {
        Location location = ctx.network.readLocation(id);
        location.updateName(newName);
    }

    @Then("the location with id {string} should have name {string}")
    public void theLocationWithIdShouldHaveName(String id, String expectedName) {
        Location location = ctx.network.readLocation(id);
        assertNotNull(location);
        assertEquals(expectedName, location.readName());
    }

    public LocationUpdateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}

