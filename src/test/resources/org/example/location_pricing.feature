Feature: Location pricing
  As an owner
  I want to set prices for a location
  So that charging sessions can be billed correctly

  Scenario: Set AC/DC prices for a location
    Given an empty charging network
    And a location with id "LOC-001" name "City Center" and address "Main Street 1" exists
    When I set an energy tariff at location "LOC-001" with AC price per kWh 0.35 EUR and DC price per kWh 0.45 EUR
    Then the energy tariff at location "LOC-001" should have AC price per kWh 0.35 EUR and DC price per kWh 0.45 EUR
