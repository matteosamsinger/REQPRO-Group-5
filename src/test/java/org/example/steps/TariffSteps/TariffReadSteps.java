package org.example.steps.TariffSteps;

import io.cucumber.java.en.Then;
import org.example.Tariff;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TariffReadSteps {
    private final ScenarioContext ctx;

    @Then("the energy tariff at location {string} should have AC price per kWh {double} EUR and DC price per kWh {double} EUR")
    public void theEnergyTariffAtLocationShouldHaveACPricePerKWhAndDCPricePerKWh(String locationId, double expectedAc, double expectedDc) {
        Tariff tariff = ctx.network.getEnergyTariffForLocation(locationId);
        assertNotNull(tariff, "No energy tariff set for location " + locationId);

        assertEquals(expectedAc, tariff.getPricePerKWhAC(), 0.0001);
        assertEquals(expectedDc, tariff.getPricePerKWhDC(), 0.0001);
    }


    public TariffReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
