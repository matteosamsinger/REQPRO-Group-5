package org.example.stepdefinitions.tariff;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Tariff;

import java.time.LocalDateTime;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TariffReadSteps {

    private Tariff lastReadTariff;

    @When("I read the tariff at location {string} at {string}")
    public void iReadTheTariffAtLocationAt(String locationId, String timeText) {
        CTX.clearLastException();
        lastReadTariff = null;

        try {
            LocalDateTime time = LocalDateTime.parse(timeText);
            lastReadTariff = CTX.getNetwork().readTariffAt(locationId, time);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("I should see tariff prices AC kWh {double} EUR, AC min {double} EUR, DC kWh {double} EUR, DC min {double} EUR")
    public void iShouldSeeTariffPrices(double acKwh, double acMin, double dcKwh, double dcMin) {
        assertNotNull(lastReadTariff, "No tariff was read");

        assertEquals(acKwh, lastReadTariff.getPricePerKWh(org.example.enums.ChargerType.AC), 0.0001);
        assertEquals(acMin, lastReadTariff.getPricePerMinute(org.example.enums.ChargerType.AC), 0.0001);

        assertEquals(dcKwh, lastReadTariff.getPricePerKWh(org.example.enums.ChargerType.DC), 0.0001);
        assertEquals(dcMin, lastReadTariff.getPricePerMinute(org.example.enums.ChargerType.DC), 0.0001);
    }
}
