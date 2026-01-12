package org.example.steps.ChargingSessionSteps;

import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.entities.Tariff;
import org.example.entities.Account;
import org.example.entities.ChargingSession;
import org.example.entities.Location;
import org.example.enums.ChargerStatus;
import org.example.steps.support.ScenarioContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChargingSessionCreateSteps {
    private final ScenarioContext ctx;

    @When("I start a charging session for client {string} at charger {string} at {string}")
    public void iStartAChargingSessionForClientAtChargerAt(String clientId, String chargerNumber, String startTimeText) {

        LocalDateTime startTime = LocalDateTime.parse(startTimeText);

        // Account
        Account account = ctx.network.findAccount(clientId);
        assertNotNull(account, "Account not found: " + clientId);
        ctx.currentAccount = account;

        // Charger
        Charger charger = findChargerByNumber(chargerNumber);
        assertNotNull(charger, "Charger not found: " + chargerNumber);
        ctx.currentCharger = charger;

        // Location kommt vom Charger
        Location location = charger.getLocation();
        assertNotNull(location, "Charger has no location assigned: " + chargerNumber);

        // Tarif
        Tariff tariff = location.readTariffAt(startTime);
        double pricePerKWhAtStart = tariff.getPricePerKWh(charger.getType());
        double pricePerMinuteAtStart = tariff.getPricePerMinute(charger.getType());

        // Session erstellen
        int sessionId = 1;
        ctx.currentSession = new ChargingSession(
                sessionId,
                account,
                charger,
                startTime,
                pricePerKWhAtStart,
                pricePerMinuteAtStart
        );

        // Status setzen (wahrscheinlich CHARGING, nicht AVAILABLE)
        ctx.currentCharger.setStatus(ChargerStatus.IN_USE);
    }


    private Charger findChargerByNumber(String chargerNumber) {
        for (Location loc : ctx.network.readAllLocations()) {
            for (Charger charger : loc.readChargers()) {
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
