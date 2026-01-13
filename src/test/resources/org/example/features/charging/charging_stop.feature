Feature: Stop charging session
  As a customer
  I want to stop a charging session
  So that I get billed correctly and the charger becomes available again

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists
    And a charger with number "1" type "AC" at location "LOC-010" exists
    And a tariff exists at location "LOC-010" with AC kWh 0.20 EUR, AC min 0.10 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And an account with id "A-010" name "Alice" and email "alice@example.com" exists
    And account "A-010" has balance 50 EUR

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Stop a session bills correctly and frees the charger
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    And I stop the current charging session at "2026-01-10T10:30" with energy 5.0 kWh
    Then the total price should be 4.0 EUR
    And the account "A-010" should have balance 46.0 EUR
    And charger "1" at location "LOC-010" should have status "AVAILABLE"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Tariff changes during session but billing uses the tariff at start
    Given a tariff exists at location "LOC-010" with AC kWh 0.10 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And a tariff exists at location "LOC-010" with AC kWh 1.00 EUR, AC min 1.00 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2026-01-10T10:30"
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    And I stop the current charging session at "2026-01-10T11:00" with energy 10.0 kWh
    Then the total price should be 4.0 EUR
    And the account "A-010" should have balance 46.0 EUR

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Stopping a non-existing session fails
    When I stop the charging session 999 at "2026-01-10T11:00" with energy 1.0 kWh
    Then I should get an error "Session not found: 999"
