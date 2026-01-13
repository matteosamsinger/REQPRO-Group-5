package org.example.stepdefinitions.tariff;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Tariff;

import java.time.LocalDateTime;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TariffSteps {

    @Given("a tariff exists at location {string} with AC kWh {double} EUR, AC min {double} EUR, DC kWh {double} EUR, DC min {double} EUR valid from {string}")
    public void aTariffExistsAtLocation(String locationId,
                                        double acKwh, double acMin,
                                        double dcKwh, double dcMin,
                                        String validFromText) {
        LocalDateTime validFrom = LocalDateTime.parse(validFromText);

        Tariff t = new Tariff(
                1,
                validFrom,
                acKwh, acMin,
                dcKwh, dcMin
        );

        CTX.getNetwork().createTariffForLocation(locationId, t);
    }

    @When("I create a tariff for location {string} valid from {string} with prices AC kWh {double} EUR, AC min {double} EUR, DC kWh {double} EUR, DC min {double} EUR")
    public void iCreateATariffForLocation(String locationId,
                                          String validFromText,
                                          double acKwh, double acMin,
                                          double dcKwh, double dcMin) {
        CTX.clearLastException();
        try {
            LocalDateTime validFrom = LocalDateTime.parse(validFromText);

            Tariff t = new Tariff(
                    1,
                    validFrom,
                    acKwh, acMin,
                    dcKwh, dcMin
            );

            CTX.getNetwork().createTariffForLocation(locationId, t);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("the tariff at location {string} at {string} should have prices AC kWh {double} EUR, AC min {double} EUR, DC kWh {double} EUR, DC min {double} EUR")
    public void theTariffShouldHavePrices(String locationId,
                                          String timeText,
                                          double expAcKwh, double expAcMin,
                                          double expDcKwh, double expDcMin) {
        LocalDateTime time = LocalDateTime.parse(timeText);

        Tariff t = CTX.getNetwork().readTariffAt(locationId, time);
        assertNotNull(t, "Tariff was null");

        assertEquals(expAcKwh, t.getPricePerKWh(org.example.enums.ChargerType.AC), 0.0001);
        assertEquals(expAcMin, t.getPricePerMinute(org.example.enums.ChargerType.AC), 0.0001);
        assertEquals(expDcKwh, t.getPricePerKWh(org.example.enums.ChargerType.DC), 0.0001);
        assertEquals(expDcMin, t.getPricePerMinute(org.example.enums.ChargerType.DC), 0.0001);
    }

    @Then("no tariff should exist at location {string} at {string}")
    public void noTariffShouldExistAt(String locationId, String timeText) {
        LocalDateTime time = LocalDateTime.parse(timeText);

        try {
            CTX.getNetwork().readTariffAt(locationId, time);
            fail("Expected no tariff, but a tariff was returned.");
        } catch (Exception ignored) {
            // passt: es gibt keinen Tarif
        }
    }
}
