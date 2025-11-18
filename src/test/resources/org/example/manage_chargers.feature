Feature: Manage chargers
  As an owner
  I want to add chargers to locations
  So that the charging network has physical chargers at each site

  Background:
    Given an empty charging network
    And a location with id "LOC-100" name "HQ" and address "HQ Street 1" exists

  Scenario: Add a charger to a location
    When I add a charger with number "1" type "AC" and max power 22 kW to location "LOC-100"
    Then the location with id "LOC-100" should have 1 charger
    And the first charger at location "LOC-100" should have type "AC"

  Scenario: Read an existing charger
    Given an empty charging network
    And a location with id "LOC-200" name "Mall" and address "Mall Street 1" exists
    And a charger with number "1" type "DC" and max power 150 kW at location "LOC-200" exists
    When I look up the charger with number "1" at location "LOC-200"
    Then I see the charger type "DC" and max power 150 kW

  Scenario: Delete a charger from a location
    Given an empty charging network
    And a location with id "LOC-300" name "Test Site" and address "Test Street 1" exists
    And a charger with number "1" type "AC" and max power 22 kW at location "LOC-300" exists
    When I delete the charger with number "1" at location "LOC-300"
    Then the location with id "LOC-300" should have 0 charger
