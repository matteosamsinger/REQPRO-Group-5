package org.example.stepdefinitions.account;

import io.cucumber.java.en.When;

import static org.example.stepdefinitions.common.CommonSteps.CTX;

public class AccountUpdateSteps {

    @When("I update account {string} name to {string} and email to {string}")
    public void iUpdateAccountNameAndEmail(String accountId, String newName, String newEmail) {
        CTX.clearLastException();
        try {
            // Wir nutzen findAccount() (liefert null wenn nicht vorhanden)
            var acc = CTX.getNetwork().findAccount(accountId);
            if (acc == null) {
                throw new IllegalArgumentException("Account not found: " + accountId);
            }

            acc.setName(newName);
            acc.setEmail(newEmail);

        } catch (Exception e) {
            CTX.setLastException(e);
        }
    }
}
