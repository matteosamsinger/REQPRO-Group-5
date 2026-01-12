package org.example.steps.TariffSteps;

import io.cucumber.java.en.Then;
import org.example.entities.Location;
import org.example.entities.Tariff;
import org.example.enums.ChargerType;
import org.example.steps.support.ScenarioContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TariffReadSteps {

    private final ScenarioContext ctx;

    public TariffReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Then("the energy tariff at location {string} should have AC price per kWh {double} EUR and DC price per kWh {double} EUR")

    public void theEnergyTariffAtLocationShouldHaveACPricePerKWhAndDCPricePerKWh(
            String locationId, double expectedAc, double expectedDc) {

        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Tariff tariff = location.readTariffAt(LocalDateTime.now());
        assertNotNull(tariff, "No energy tariff set for location: " + locationId);

        assertEquals(expectedAc, tariff.getPricePerKWh(ChargerType.AC), 0.0001);
        assertEquals(expectedDc, tariff.getPricePerKWh(ChargerType.DC), 0.0001);
    }
}

