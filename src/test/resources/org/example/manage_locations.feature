Feature: Manage locations
  As an owner
  I want to create locations
  So that I can configure the charging network

  Scenario: Create a new location
    Given an empty charging network
    When I create a location with id "LOC-001" name "City Center" and address "Main Street 1"
    Then there should be a location with id "LOC-001" and name "City Center"
