Feature: Manage locations
  As an owner
  I want to create locations
  So that I can configure the charging network

  Scenario: Create a new location
    Given an empty charging network
    When I create a location with id "LOC-001" name "City Center" and address "Main Street 1"
    Then there should be a location with id "LOC-001" and name "City Center"

  Scenario: Read an existing location
    Given an empty charging network
    And a location with id "LOC-002" name "Airport" and address "Airport Road 1" exists
    When I look up the location with id "LOC-002"
    Then I see the location name "Airport" and address "Airport Road 1"

  Scenario: Update a location name
    Given an empty charging network
    And a location with id "LOC-003" name "Old Name" and address "Old Street 1" exists
    When I change the name of the location with id "LOC-003" to "New Name"
    Then the location with id "LOC-003" should have name "New Name"

  Scenario: Delete a location
    Given an empty charging network
    And a location with id "LOC-004" name "To Delete" and address "Some Street 1" exists
    When I delete the location with id "LOC-004"
    Then there should be no location with id "LOC-004"

