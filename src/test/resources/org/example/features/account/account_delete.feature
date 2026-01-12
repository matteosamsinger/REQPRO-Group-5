Feature: Delete account
  As an owner
  I want to delete accounts
  So that inactive users can be removed

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Delete an existing account
    Given an account with id "A-001" name "Alice" and email "alice@example.com" exists
    When I delete the account "A-001"
    Then there should be no account with id "A-001"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Deleting an unknown account does nothing (idempotent)
    When I delete the account "A-404"
    And I read the account "A-404"
    Then I should get an error "Account not found: A-404"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Deleting an account with an active charging session fails
    Given a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists
    And a charger with number "1" type "AC" at location "LOC-010" exists
    And a tariff exists at location "LOC-010" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And an account with id "A-010" name "Alice" and email "alice@example.com" exists
    And account "A-010" has balance 50 EUR
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    And I delete the account "A-010"
    Then I should get an error "Cannot delete account A-010: active charging session exists"