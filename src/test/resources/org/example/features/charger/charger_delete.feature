Feature: Delete charger
  As an owner
  I want to delete chargers from a location
  So that I can manage my charging network

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "Site A" and address "Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Happy - delete an existing charger
    Given a charger with number "1" type "AC" at location "LOC-010" exists
    When I delete the charger with number "1" at location "LOC-010"
    Then the location "LOC-010" should have 0 charger

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Edge - delete last remaining charger
    Given a charger with number "1" type "AC" at location "LOC-010" exists
    When I delete the charger with number "1" at location "LOC-010"
    Then the location "LOC-010" should have 0 charger

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Error - cannot delete a charger that is currently charging
    Given a charger with number "1" type "AC" at location "LOC-010" exists
    And charger "1" at location "LOC-010" is currently charging
    When I try to delete the charger with number "1" at location "LOC-010"
    Then I should get an error "Cannot delete charger 1 at location LOC-010 because it is currently in use"