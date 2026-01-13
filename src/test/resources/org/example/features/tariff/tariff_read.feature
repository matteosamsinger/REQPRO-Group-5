Feature: Read tariff
  As a customer
  I want to read tariffs for a location
  So that I know what prices apply at a given time

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "HQ" and address "HQ Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Read the current tariff
    Given a tariff exists at location "LOC-010" with AC kWh 0.20 EUR, AC min 0.10 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And a tariff exists at location "LOC-010" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.70 EUR, DC min 0.15 EUR valid from "2026-01-01T00:00"
    When I read the tariff at location "LOC-010" at "2026-01-10T10:00"
    Then I should see tariff prices AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.70 EUR, DC min 0.15 EUR

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Read a tariff from the past
    Given a tariff exists at location "LOC-010" with AC kWh 0.10 EUR, AC min 0.05 EUR, DC kWh 0.50 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And a tariff exists at location "LOC-010" with AC kWh 0.40 EUR, AC min 0.20 EUR, DC kWh 0.80 EUR, DC min 0.30 EUR valid from "2026-01-01T00:00"
    When I read the tariff at location "LOC-010" at "2025-12-31T23:59"
    Then I should see tariff prices AC kWh 0.10 EUR, AC min 0.05 EUR, DC kWh 0.50 EUR, DC min 0.10 EUR

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Reading a tariff when none exists fails
    When I read the tariff at location "LOC-010" at "2026-01-10T10:00"
    Then I should get an error "No tariff defined for location LOC-010 at 2026-01-10T10:00"
