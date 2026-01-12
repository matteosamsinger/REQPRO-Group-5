Feature: Update charger
  As an owner
  I want to update charger data
  So that I can adapt the charging infrastructure

  Background:
    Given an empty charging network
    And a location with id "LOC-010" name "Site A" and address "Street 1" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Update charger type from AC to DC
    Given a charger with number "1" type "AC" at location "LOC-010" exists
    When I update the charger "1" at location "LOC-010" type to "DC"
    Then the charger "1" at location "LOC-010" should have type "DC"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Updating a charger type while it is in use fails
    Given a charger with number "1" type "AC" at location "LOC-010" exists
    And charger "1" at location "LOC-010" is currently charging
    When I update the charger "1" at location "LOC-010" type to "DC"
    Then I should get an error "Cannot update charger 1 at location LOC-010: charger is in use"
    And the charger "1" at location "LOC-010" should have type "AC"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Updating a non-existing charger fails
    When I update the charger "99" at location "LOC-010" type to "DC"
    Then I should get an error "Charger not found at location LOC-010: 99"
