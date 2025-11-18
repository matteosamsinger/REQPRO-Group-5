package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;


import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private ElectricChargingStationNetwork network;

    @Given("an empty charging network")
    public void anEmptyChargingNetwork() {
        network = new ElectricChargingStationNetwork();
    }

    @When("I create a location with id {string} name {string} and address {string}")
    public void iCreateALocationWithIdNameAndAddress(String id, String name, String address) {
        Location location = new Location(id, name, address);
        network.addLocation(location);
    }

    @Then("there should be a location with id {string} and name {string}")
    public void thereShouldBeALocationWithIdAndName(String id, String expectedName) {
        Location found = network.findLocation(id);
        assertNotNull(found);
        assertEquals(expectedName, found.getName());
    }
}

