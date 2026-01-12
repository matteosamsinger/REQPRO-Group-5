Feature: Start charging session
  As a customer
  I want to start a charging session
  So that I can charge my vehicle

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists
    And a charger with number "1" type "AC" at location "LOC-010" exists
    And an account with id "A-010" name "Alice" and email "alice@example.com" exists
    And an account with id "A-020" name "Alex" and email "alex@example.com" exists
    And account "A-010" has balance 50 EUR
    And account "A-020" has balance 50 EUR

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Start a charging session successfully
    And a tariff exists at location "LOC-010" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    Then a charging session should be started
    And charger "1" at location "LOC-010" should have status "IN_USE"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Starting a second session on the same charger fails
    And a tariff exists at location "LOC-010" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    And I start a charging session for account "A-020" at location "LOC-010" charger "1" at "2026-01-10T10:05"
    Then I should get an error "Charger not available: 1"
    And charger "1" at location "LOC-010" should have status "IN_USE"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Starting a session without any tariff fails
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    Then I should get an error "No tariff defined for location LOC-010 at 2026-01-10T10:00"
    And charger "1" at location "LOC-010" should have status "AVAILABLE"
