package org.example.stepdefinitions.common;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.example.app.ElectricChargingStationNetwork;
import org.example.stepdefinitions.support.TestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommonSteps {

    // einfacher globaler Context (ohne DI)
    public static final TestContext CTX = new TestContext();

    @Given("an empty charging network")
    public void anEmptyChargingNetwork() {
        CTX.setNetwork(new ElectricChargingStationNetwork());
        CTX.clearLastException();
    }

    @Then("I should get an error {string}")
    public void iShouldGetAnError(String expectedMessage) {
        Exception ex = CTX.getLastException();
        assertNotNull(ex, "Expected an error, but no exception was captured.");
        assertEquals(expectedMessage, ex.getMessage());
    }
}
