package org.example.steps.NetworkStatusSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.Charger;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkStatusReadSteps {
    private final ScenarioContext ctx;

    @When("I request the network status")
    public void iRequestTheNetworkStatus() {
        ctx.lastNetworkStatus = ctx.network.getNetworkStatus();
    }

    @Then("I see {int} charger status entries")
    public void iSeeChargerStatusEntries(int expectedCount) {
        assertNotNull(ctx.lastNetworkStatus, "Network status was not requested yet");
        assertEquals(expectedCount, ctx.lastNetworkStatus.size());
    }

    @And("one entry for charger {string} has status {string}")
    public void oneEntryForChargerHasStatus(String chargerNumber, String expectedStatus) {
        assertNotNull(ctx.lastNetworkStatus, "Network status was not requested yet");

        boolean found = false;
        for (Charger c : ctx.lastNetworkStatus) {
            if (c.getNumber().equals(chargerNumber)
                    && expectedStatus.equals(c.getStatus())) {
                found = true;
                break;
            }
        }

        assertTrue(found,
                "No charger " + chargerNumber + " with status " + expectedStatus + " found in network status");
    }

    public NetworkStatusReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

}
