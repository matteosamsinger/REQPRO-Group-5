package org.example.stepdefinitions.network;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkStatusSteps {

    private String lastStatus;

    @When("I read the network status")
    public void iReadTheNetworkStatus() {
        CTX.clearLastException();
        try {
            lastStatus = CTX.getNetwork().toNetworkStatusString();
        } catch (Exception e) {
            CTX.setLastException(e);
            lastStatus = null;
        }
    }

    @Then("the network status should include location {string} with name {string}")
    public void theNetworkStatusShouldIncludeLocationWithName(String locationId, String name) {
        assertNotNull(lastStatus, "No network status was read");
        assertTrue(lastStatus.contains("Location " + locationId + " | " + name), "Expected location line missing. Status was:\n" + lastStatus);
    }

    @Then("the network status should show charger {string} at location {string} has status {string}")
    public void theNetworkStatusShouldShowChargerStatus(String chargerNumber, String locationId, String expectedStatus) {
        assertNotNull(lastStatus, "No network status was read");

        String locationHeader = "Location " + locationId + " |";
        int start = lastStatus.indexOf(locationHeader);
        assertTrue(start >= 0, "Location not found in status: " + locationId + "\nStatus was:\n" + lastStatus);

        int nextLoc = lastStatus.indexOf("\nLocation ", start + 1);
        String block = (nextLoc >= 0) ? lastStatus.substring(start, nextLoc) : lastStatus.substring(start);

        String expectedLine = "- #" + chargerNumber + " |";
        assertTrue(
                block.contains(expectedLine) && block.contains("status=" + expectedStatus),
                "Expected charger status missing for charger #" + chargerNumber + " at " + locationId +
                        ".\nLocation block was:\n" + block
        );
    }


    @Then("the network status should show prices are not set for location {string}")
    public void theNetworkStatusShouldShowPricesNotSetForLocation(String locationId) {
        assertNotNull(lastStatus, "No network status was read");

        String locationHeader = "Location " + locationId + " |";
        int start = lastStatus.indexOf(locationHeader);
        assertTrue(start >= 0, "Location not found in status: " + locationId + "\nStatus was:\n" + lastStatus);

        int nextLoc = lastStatus.indexOf("\nLocation ", start + 1);
        String block = (nextLoc >= 0) ? lastStatus.substring(start, nextLoc) : lastStatus.substring(start);

        assertTrue(block.contains("Prices: (no tariff set)"),
                "Expected 'Prices: (no tariff set)' missing for " + locationId + ".\nLocation block was:\n" + block);
    }


    @Then("the network status should show no locations")
    public void theNetworkStatusShouldShowNoLocations() {
        assertNotNull(lastStatus, "No network status was read");
        assertTrue(lastStatus.contains("(no locations)"), "Expected '(no locations)' missing.\nStatus was:\n" + lastStatus);
    }
}