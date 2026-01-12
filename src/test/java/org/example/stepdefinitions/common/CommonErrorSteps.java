package org.example.stepdefinitions.common;

import io.cucumber.java.en.Then;

import static org.example.stepdefinitions.common.CommonSteps.CTX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommonErrorSteps {

    private static Throwable lastError;

    @Then("an error should occur with message {string}")
    public void anErrorShouldOccurWithMessage(String expectedMessage) {
        Exception ex = CTX.getLastException();
        assertNotNull(ex, "Expected an error, but none was captured.");
        assertEquals(expectedMessage, ex.getMessage());
        CTX.clearLastException(); // reset
    }
}
