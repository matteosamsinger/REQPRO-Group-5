Feature: Network status
  As an owner
  I want to see the current network status
  So that I can monitor locations, tariffs and charger availability

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Show network status for multiple locations with tariffs and different charger states
    Given a location with id "LOC-001" name "City Center" and address "Main Street 1" exists
    And a location with id "LOC-002" name "Mall" and address "Mall Street 2" exists

    And a tariff exists at location "LOC-001" with AC kWh 0.35 EUR, AC min 0.05 EUR, DC kWh 0.60 EUR, DC min 0.10 EUR valid from "2000-01-01T00:00"
    And a tariff exists at location "LOC-002" with AC kWh 0.30 EUR, AC min 0.04 EUR, DC kWh 0.55 EUR, DC min 0.09 EUR valid from "2000-01-01T00:00"

    And a charger with number "1" type "AC" at location "LOC-001" exists
    And a charger with number "2" type "DC" at location "LOC-001" exists
    And a charger with number "1" type "AC" at location "LOC-002" exists
    And a charger with number "2" type "DC" at location "LOC-002" exists

    And charger "1" at location "LOC-001" is currently charging
    And charger "2" at location "LOC-002" is out of order


    When I read the network status
    Then the network status should include location "LOC-001" with name "City Center"
    And the network status should include location "LOC-002" with name "Mall"
    And the network status should show charger "1" at location "LOC-001" has status "IN_USE"
    And the network status should show charger "2" at location "LOC-001" has status "AVAILABLE"
    And the network status should show charger "1" at location "LOC-002" has status "AVAILABLE"
    And the network status should show charger "2" at location "LOC-002" has status "OUT_OF_ORDER"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Location without any tariff is shown with "no tariff set"
    Given a location with id "LOC-100" name "NoTariffSite" and address "Nowhere 1" exists
    And a charger with number "1" type "AC" at location "LOC-100" exists
    When I read the network status
    Then the network status should include location "LOC-100" with name "NoTariffSite"
    And the network status should show prices are not set for location "LOC-100"
    And the network status should show charger "1" at location "LOC-100" has status "AVAILABLE"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Network status with no locations
    When I read the network status
    Then the network status should show no locations
