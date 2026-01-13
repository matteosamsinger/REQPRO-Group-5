Feature: Create charger
  As an owner
  I want to add chargers to locations
  So that customers can charge at specific sites

  Background:
    Given an empty charging network
    And a location with id "LOC-001" name "City Center" and address "Main Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Add a new charger to a location
    When I add a charger with number "1" type "AC" to location "LOC-001"
    Then the location "LOC-001" should have 1 charger
    And the charger "1" at location "LOC-001" should have type "AC"

  # -------------------------
  # Edge Case
  # (same charger number is allowed at different locations)
  # -------------------------
  Scenario: Same charger number at different locations is allowed
    Given a location with id "LOC-002" name "Mall" and address "Mall Street 2" exists
    When I add a charger with number "1" type "AC" to location "LOC-001"
    And I add a charger with number "1" type "DC" to location "LOC-002"
    Then the location "LOC-001" should have 1 charger
    And the location "LOC-002" should have 1 charger
    And the charger "1" at location "LOC-001" should have type "AC"
    And the charger "1" at location "LOC-002" should have type "DC"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Adding a duplicate charger number at the same location fails
    Given I add a charger with number "7" type "AC" to location "LOC-001"
    When I add a charger with number "7" type "DC" to location "LOC-001"
    Then I should get an error "Charger number already exists at location LOC-001: 7"
    And the location "LOC-001" should have 1 charger