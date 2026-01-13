Feature: Update location
  As an owner
  I want to update location data
  So that the network information stays correct

  Background:
    Given an empty charging network
    And a location with id "LOC-001" name "City Center" and address "Main Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Update location name successfully
    When I update the location "LOC-001" name to "Downtown"
    Then there should be a location with id "LOC-001" and name "Downtown"
    And I see the location name "Downtown" and address "Main Street 1" for location "LOC-001"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Update location name to the same value (no change)
    When I update the location "LOC-001" name to "City Center"
    Then there should be a location with id "LOC-001" and name "City Center"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Update name of unknown location fails
    When I update the location "LOC-404" name to "Ghost"
    Then I should get an error "Location not found: LOC-404"