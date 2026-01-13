package org.example.stepdefinitions.location;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationReadSteps {

    private Location lastReadLocation;

    @When("I read the location {string}")
    public void iReadTheLocation(String locationId) {
        CTX.clearLastException();
        lastReadLocation = null;

        try {
            Location found = CTX.getNetwork().readLocation(locationId);
            if (found == null) {
                throw new IllegalArgumentException("Location not found: " + locationId);
            }
            lastReadLocation = found;
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("I should see location id {string} name {string} and address {string}")
    public void iShouldSeeLocation(String id, String expectedName, String expectedAddress) {
        assertNotNull(lastReadLocation, "No location was read");
        assertEquals(id, lastReadLocation.readId());
        assertEquals(expectedName, lastReadLocation.readName());
        assertEquals(expectedAddress, lastReadLocation.readAddress());
    }
}
