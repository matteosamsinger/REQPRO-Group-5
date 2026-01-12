Feature: Client invoice
  As a customer
  I want to see my invoice
  So that I can review top-ups and charging costs

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Invoice shows top-ups and one charging item
    Given a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists
    And a charger with number "1" type "AC" at location "LOC-010" exists
    And a tariff exists at location "LOC-010" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"

    And an account with id "A-010" name "Alice" and email "alice@example.com" exists
    When I top up account "A-010" by 20 EUR
    And I top up account "A-010" by 30 EUR

    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    And I stop the current charging session at "2026-01-10T10:10" with energy 5.0 kWh

    When I read the client invoice for account "A-010"
    Then the invoice should be for account "A-010" name "Alice" email "alice@example.com"
    And the invoice should show 2 top-up
    And the invoice should show 1 charging invoice item

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Invoice shows no top-ups and no charging items
    Given an account with id "A-001" name "Empty" and email "empty@example.com" exists
    When I read the client invoice for account "A-001"
    Then the invoice should be for account "A-001" name "Empty" email "empty@example.com"
    And the invoice should show 0 top-up
    And the invoice should show 0 charging invoice item

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Reading invoice of unknown account fails
    When I read the client invoice for account "A-404"
    Then I should get an error "Account not found: A-404"
