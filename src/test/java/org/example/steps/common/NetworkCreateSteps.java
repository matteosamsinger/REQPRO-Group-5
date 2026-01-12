package org.example.steps.common;

import io.cucumber.java.en.Given;
import org.example.app.ElectricChargingStationNetwork;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NetworkCreateSteps {
    private final ScenarioContext ctx;

    public NetworkCreateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Given("an empty charging network")
    public void anEmptyChargingNetwork() {
        ctx.network = new ElectricChargingStationNetwork();
        assertNotNull(ctx.network);
    }
}

