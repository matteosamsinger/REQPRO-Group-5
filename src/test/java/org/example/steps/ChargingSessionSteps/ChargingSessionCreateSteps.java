package org.example.steps.ChargingSessionSteps;

import io.cucumber.java.en.When;
import org.example.Charger;
import org.example.ChargingSession;
import org.example.Location;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargingSessionCreateSteps {
    private final ScenarioContext ctx;

    @When("I start a charging session for client {string} at charger {string} at {string}")
    public void iStartAChargingSessionForClientAtChargerAt(String clientId, String chargerNumber, String startTimeText) {
        ctx.currentClient = ctx.network.findClient(clientId);
        assertNotNull(ctx.currentClient, "Client not found: " + clientId);

        ctx.currentCharger = findChargerByNumber(chargerNumber);
        assertNotNull(ctx.currentCharger, "Charger not found: " + chargerNumber);

        java.time.LocalDateTime startTime = java.time.LocalDateTime.parse(startTimeText);

        ctx.currentSession = new ChargingSession(1, ctx.currentClient, ctx.currentCharger, startTime);

        // Charger ist jetzt im Status "CHARGING"
        ctx.currentCharger.setStatus("CHARGING");
    }

    private Charger findChargerByNumber(String chargerNumber) {
        for (Location loc : ctx.network.getAllLocations()) {
            for (Charger charger : loc.getChargers()) {
                if (charger.getNumber().equals(chargerNumber)) return charger;
            }
        }
        return null;
    }

    public ChargingSessionCreateSteps(ScenarioContext ctx)
    {
        this.ctx=ctx;
    }
}
