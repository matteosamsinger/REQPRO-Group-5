Feature: Create location
  As an owner
  I want to create locations
  So that I can manage charging sites in the network


  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Create a new location successfully
    When I create a location with id "LOC-001" name "City Center" and address "Main Street 1"
    Then there should be a location with id "LOC-001" and name "City Center"
    And I see the location name "City Center" and address "Main Street 1" for location "LOC-001"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Create a location with minimal data (short name, short address)
    When I create a location with id "L1" name "A" and address "B"
    Then there should be a location with id "L1" and name "A"
    And I see the location name "A" and address "B" for location "L1"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Creating a location with an existing id fails
    Given a location with id "LOC-009" name "Old" and address "Old Street 9" exists
    When I create a location with id "LOC-009" name "New" and address "New Street 9"
    Then I should get an error "Location already exists: LOC-009"
    And I see the location name "Old" and address "Old Street 9" for location "LOC-009"