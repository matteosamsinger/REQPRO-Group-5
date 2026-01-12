Feature: Create tariff
  As an owner
  I want to create tariffs for locations
  So that charging sessions can be priced correctly

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Create a normal tariff
    When I create a tariff for location "LOC-010" valid from "2000-01-01T00:00" with prices AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR
    Then the tariff at location "LOC-010" at "2000-01-01T00:00" should have prices AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Create a free tariff (all prices zero)
    When I create a tariff for location "LOC-010" valid from "2000-01-01T00:00" with prices AC kWh 0.00 EUR, AC min 0.00 EUR, DC kWh 0.00 EUR, DC min 0.00 EUR
    Then the tariff at location "LOC-010" at "2000-01-01T00:00" should have prices AC kWh 0.00 EUR, AC min 0.00 EUR, DC kWh 0.00 EUR, DC min 0.00 EUR

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Creating a tariff with negative prices fails
    When I create a tariff for location "LOC-010" valid from "2000-01-01T00:00" with prices AC kWh -0.01 EUR, AC min 0.00 EUR, DC kWh 0.00 EUR, DC min 0.00 EUR
    Then I should get an error "Tariff prices must be >= 0"
    And no tariff should exist at location "LOC-010" at "2000-01-01T00:00"