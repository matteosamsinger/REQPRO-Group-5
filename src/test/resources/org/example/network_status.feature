Feature: Network status
  As an owner
  I want to see the current status of all chargers
  So that I can monitor the charging network

  Background:
    Given an empty charging network
    And a location with id "LOC-100" name "City Center" and address "Main Street 1" exists
    And a charger with number "1" type "AC" and max power 22 kW at location "LOC-100" exists
    And a charger with number "2" type "DC" and max power 150 kW at location "LOC-100" exists

  Scenario: See list of charger statuses
    Given charger "1" at location "LOC-100" is currently charging
    And charger "2" at location "LOC-100" is available
    When I request the network status
    Then I see 2 charger status entries
    And one entry for charger "1" has status "CHARGING"
    And one entry for charger "2" has status "AVAILABLE"