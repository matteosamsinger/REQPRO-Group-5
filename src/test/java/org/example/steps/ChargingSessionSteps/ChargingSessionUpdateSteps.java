package org.example.steps.ChargingSessionSteps;

import io.cucumber.java.en.And;
import org.example.Account;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargingSessionUpdateSteps {
    private final ScenarioContext ctx;

    @And("I stop the charging session at {string} with energy {double} kWh and pricePerKWh {double} EUR")
    public void iStopTheChargingSessionAtWithEnergyKWhAndPricePerKWhEur(String endTimeText, double energyKWh, double pricePerKWh) {
        assertNotNull(ctx.currentSession, "No current charging session");

        java.time.LocalDateTime endTime = java.time.LocalDateTime.parse(endTimeText);

        // Session beenden und Preis berechnen
        ctx.currentSession.stop(endTime, energyKWh, pricePerKWh);

        // Betrag vom Account abbuchen
        Account account = ctx.currentClient.getAccount();
        account.debit(ctx.currentSession.getTotalPrice());

        // Charger wieder verfügbar
        ctx.currentCharger.setStatus("AVAILABLE");
    }


    public ChargingSessionUpdateSteps(ScenarioContext ctx)
    {
        this.ctx=ctx;
    }
}
