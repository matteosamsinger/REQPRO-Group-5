package org.example.steps.TariffSteps;

import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.entities.Tariff;
import org.example.steps.support.ScenarioContext;
import java.time.LocalDateTime;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TariffUpdateSteps {
    private final ScenarioContext ctx;

    @When("I set an energy tariff at location {string} with AC price per kWh {double} EUR and DC price per kWh {double} EUR")

    public void iSetAnEnergyTariffAtLocationWithACPricePerKWhAndDCPricePerKWh(
            String locationId, double acPrice, double dcPrice) {

        Location location = ctx.network.readLocation(locationId);
        assertNotNull(location, "Location not found: " + locationId);

        // Tariff braucht LocalDateTime + Minute-Preise (die setzen wir hier erstmal auf 0.0)
        Tariff tariff = new Tariff(
                1,
                LocalDateTime.parse("2000-01-01T00:00"),
                // sicher "gültig" ab jetzt
                acPrice,
                0.0,
                dcPrice,
                0.0
        );

        location.createTariff(tariff);
    }

    public TariffUpdateSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }
}
