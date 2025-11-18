package org.example;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;


import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private ElectricChargingStationNetwork network;
    private Location lookedUpLocation;

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

    @Given("a location with id {string} name {string} and address {string} exists")
    public void aLocationWithIdNameAndAddressExists(String id, String name, String address) {
        Location location = new Location(id, name, address);
        network.addLocation(location);
    }

    @When("I look up the location with id {string}")
    public void iLookUpTheLocationWithId(String id) {
        lookedUpLocation = network.findLocation(id);
    }

    @Then("I see the location name {string} and address {string}")
    public void iSeeTheLocationNameAndAddress(String expectedName, String expectedAddress) {
        assertNotNull(lookedUpLocation);
        assertEquals(expectedName, lookedUpLocation.getName());
        assertEquals(expectedAddress, lookedUpLocation.getAddress());
    }

    @When("I change the name of the location with id {string} to {string}")
    public void iChangeTheNameOfTheLocationWithIdTo(String id, String newName) {
        Location location = network.findLocation(id);
        location.setName(newName);
    }

    @Then("the location with id {string} should have name {string}")
    public void theLocationWithIdShouldHaveName(String id, String expectedName) {
        Location location = network.findLocation(id);
        assertNotNull(location);
        assertEquals(expectedName, location.getName());
    }

    @When("I delete the location with id {string}")
    public void iDeleteTheLocationWithId(String id) {
        network.deleteLocation(id);
    }

    @Then("there should be no location with id {string}")
    public void thereShouldBeNoLocationWithId(String id) {
        Location location = network.findLocation(id);
        assertNull(location);
    }

}

