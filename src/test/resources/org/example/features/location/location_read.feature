Feature: Read location
  As an owner
  I want to read location details
  So that I can view name and address of a site

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Read an existing location
    Given a location with id "LOC-001" name "City Center" and address "Main Street 1" exists
    When I read the location "LOC-001"
    Then I should see location id "LOC-001" name "City Center" and address "Main Street 1"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Read one location when multiple exist
    Given a location with id "LOC-001" name "City Center" and address "Main Street 1" exists
    And a location with id "LOC-002" name "Mall" and address "Mall Street 2" exists
    When I read the location "LOC-002"
    Then I should see location id "LOC-002" name "Mall" and address "Mall Street 2"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Reading an unknown location fails
    When I read the location "LOC-404"
    Then I should get an error "Location not found: LOC-404"
