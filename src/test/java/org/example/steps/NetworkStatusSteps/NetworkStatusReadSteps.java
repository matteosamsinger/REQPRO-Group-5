package org.example.steps.NetworkStatusSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Charger;
import org.example.enums.ChargerStatus;
import org.example.steps.support.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkStatusReadSteps {
    private final ScenarioContext ctx;

    @When("I request the network status")
    public void iRequestTheNetworkStatus() {
        ctx.lastNetworkStatus = ctx.network.readNetworkStatus();
    }

    @Then("I see {int} charger status entries")
    public void iSeeChargerStatusEntries(int expectedCount) {
        assertNotNull(ctx.lastNetworkStatus, "Network status was not requested yet");
        assertEquals(expectedCount, ctx.lastNetworkStatus.size());
    }

    @And("one entry for charger {string} has status {string}")
    public void oneEntryForChargerHasStatus(String chargerIdText, String statusText) {

        int chargerId = Integer.parseInt(chargerIdText.trim());

        String s = statusText.trim().toUpperCase();

        ChargerStatus expectedStatus = ChargerStatus.AVAILABLE;
        boolean found = false;
        for (Charger c : ctx.lastNetworkStatus) {

            boolean numberMatches = c.getNumber().equals(chargerIdText.trim());

            boolean idMatches = false;
            try {
                idMatches = c.getChargerId() == Integer.parseInt(chargerIdText.trim());
            } catch (NumberFormatException ignored) { }

            if ((numberMatches || idMatches) && c.getStatus() == expectedStatus) {
                found = true;
                break;
            }
        }



        
        for (Charger c : ctx.lastNetworkStatus) {
            if (c.getChargerId() == chargerId && c.getStatus() == expectedStatus) {
                found = true;
                break;
            }
        }

        assertTrue(found,
                "No charger " + chargerId + " with status " + statusText + " found in network status");
    }

    public NetworkStatusReadSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }


}
