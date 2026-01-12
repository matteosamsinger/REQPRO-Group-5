package org.example.steps.ChargingSessionSteps;

import io.cucumber.java.en.And;
import org.example.entities.Account;
import org.example.steps.support.ScenarioContext;
import org.example.enums.ChargerStatus;


import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargingSessionUpdateSteps {
    private final ScenarioContext ctx;

    @And("I stop the charging session at {string} with energy {double} kWh and pricePerKWh {double} EUR")
    public void iStopTheChargingSessionAtWithEnergyKWhAndPricePerKWhEur(String endTimeText, double energyKWh, double pricePerKWh) {
        assertNotNull(ctx.currentSession, "No current charging session");

        java.time.LocalDateTime endTime = java.time.LocalDateTime.parse(endTimeText);

        // Session beenden und Preis berechnen
        ctx.currentSession.stop(endTime, energyKWh);


        // Betrag vom Account abbuchen
        ctx.currentAccount.debit(ctx.currentSession.getTotalPrice());


        // Charger wieder verfügbar
        ctx.currentCharger.setStatus(ChargerStatus.AVAILABLE);

    }


    public ChargingSessionUpdateSteps(ScenarioContext ctx)
    {
        this.ctx=ctx;
    }
}
