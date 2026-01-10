package org.example.steps.TariffSteps;

import io.cucumber.java.en.When;
import org.example.Location;
import org.example.Tariff;
import org.example.steps.support.ScenarioContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TariffUpdateSteps {
    private final ScenarioContext ctx;

    @When("I set an energy tariff at location {string} with AC price per kWh {double} EUR and DC price per kWh {double} EUR")
    public void iSetAnEnergyTariffAtLocationWithACPricePerKWhAndDCPricePerKWh(String locationId, double acPrice, double dcPrice) {
        Location location = ctx.network.findLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        Tariff tariff = new Tariff(
                1,                // tariffId (hier einfach 1, reicht für MVP)
                LocalDate.now(),  // validFrom
                acPrice,          // pricePerKWhAC
                0.0,              // pricePerMinuteAC
                dcPrice,          // pricePerKWhDC
                0.0,              // pricePerMinuteDC
                location          // location
        );

        ctx.network.setEnergyTariffForLocation(locationId, tariff);
    }

    public TariffUpdateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
