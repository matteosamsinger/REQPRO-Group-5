package org.example.steps.ChargerSteps;

import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Location;
import org.example.steps.support.ScenarioContext;

public class ChargerDeleteSteps {
    private final ScenarioContext ctx;

    @When("I delete the charger with number {string} at location {string}")
    public void iDeleteTheChargerWithNumberAtLocation(String number, String locationId) {
        ctx.network.deleteChargerFromLocation(locationId, number);
    }

    public ChargerDeleteSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}