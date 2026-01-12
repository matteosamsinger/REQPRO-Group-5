Feature: Delete location
  As an owner
  I want to delete locations
  So that the network stays clean

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Delete an existing location
    Given a location with id "LOC-001" name "City Center" and address "Main Street 1" exists
    When I delete the location "LOC-001"
    Then there should be no location with id "LOC-001"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Deleting an unknown location does nothing (idempotent)
    When I delete the location "LOC-404"
    Then there should be no location with id "LOC-404"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Deleting a location with an active charging session fails
    Given a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists
    And a charger with number "1" type "AC" at location "LOC-010" exists
    And a tariff exists at location "LOC-010" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And an account with id "A-010" name "Alice" and email "alice@example.com" exists
    And account "A-010" has balance 50 EUR
    When I start a charging session for account "A-010" at location "LOC-010" charger "1" at "2026-01-10T10:00"
    And I delete the location "LOC-010"
    Then I should get an error "Cannot delete location LOC-010: active charging session exists"
    And there should be a location with id "LOC-010" and name "HQ"