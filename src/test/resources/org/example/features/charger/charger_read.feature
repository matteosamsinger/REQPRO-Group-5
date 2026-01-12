Feature: Read charger
  As an owner
  I want to read charger details
  So that I can inspect configuration and availability

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "Site A" and address "Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Read an available charger at a location
    Given a charger with number "1" type "AC" at location "LOC-010" exists
    When I read the charger "1" at location "LOC-010"
    Then I should see charger id 1 number "1" type "AC" at location "LOC-010"
    And the read charger should have status "AVAILABLE"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Read a charger object that currently has no location assigned (standalone test object)
    Given a standalone charger with id 5 number "9" type "DC" exists
    When I read the standalone charger "9"
    Then I should see charger id 5 number "9" type "DC" with no location
    And the read charger should have status "AVAILABLE"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Reading a non-existing charger fails
    When I read the charger "99" at location "LOC-010"
    Then I should get an error "Charger not found at location LOC-010: 99"
