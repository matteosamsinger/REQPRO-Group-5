package org.example.stepdefinitions.charging;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.ChargingSession;

import java.time.LocalDateTime;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.*;

public class ChargingStartSteps {

    private ChargingSession currentSession;
    private ChargingSession lastStoppedSession;


    @When("I start a charging session for account {string} at location {string} charger {string} at {string}")
    public void iStartAChargingSession(String accountId, String locationId, String chargerNumber, String startTimeText) {
        CTX.clearLastException();
        currentSession = null;

        try {
            LocalDateTime start = LocalDateTime.parse(startTimeText);
            currentSession = CTX.getNetwork().startChargingSession(accountId, locationId, chargerNumber, start);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }


    public ChargingSession getCurrentSession() {
        return currentSession;
    }

    @Then("a charging session should be started")
    public void aChargingSessionShouldBeStarted() {
        assertNotNull(currentSession, "No charging session was started");
    }

    @When("I stop the current charging session at {string} with energy {double} kWh")
    public void iStopTheCurrentChargingSession(String endTimeText, double energyKWh) {
        CTX.clearLastException();
        lastStoppedSession = null;

        try {
            assertNotNull(currentSession, "No current session to stop");
            LocalDateTime end = LocalDateTime.parse(endTimeText);

            lastStoppedSession = CTX.getNetwork().stopChargingSession(
                    currentSession.getSessionId(),
                    end,
                    energyKWh
            );
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @When("I stop the charging session {int} at {string} with energy {double} kWh")
    public void iStopTheChargingSessionById(int sessionId, String endTimeText, double energyKWh) {
        CTX.clearLastException();
        lastStoppedSession = null;

        try {
            LocalDateTime end = LocalDateTime.parse(endTimeText);
            lastStoppedSession = CTX.getNetwork().stopChargingSession(sessionId, end, energyKWh);
        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }

    @Then("the total price should be {double} EUR")
    public void theTotalPriceShouldBe(double expected) {
        assertNotNull(lastStoppedSession, "No session was stopped");
        assertEquals(expected, lastStoppedSession.getTotalPrice(), 0.0001);
    }
}
