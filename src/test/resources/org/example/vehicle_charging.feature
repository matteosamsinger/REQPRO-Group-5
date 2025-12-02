Feature: Vehicle charging
  As a customer
  I want to charge my vehicle
  So that my account balance is used to pay for charging sessions

  Background:
    Given an empty charging network
    And a location with id "LOC-001" name "City Center" and address "Main Street 1" exists
    And a charger with number "1" type "AC" and max power 22 kW at location "LOC-001" exists
    And a client with id "C-001" name "Alice" and email "alice@example.com" and an empty account exists
    And the account of client "C-001" has a balance of 20 EUR

  Scenario: Start and stop an AC charging session and bill the customer
    When I start a charging session for client "C-001" at charger "1" at "2025-02-01T10:00"
    And I stop the charging session at "2025-02-01T11:00" with energy 10 kWh and pricePerKWh 0.35 EUR
    Then the charging session should have a duration of 60 minutes
    And the total price of the charging session should be 3.5 EUR
    And the account balance of client "C-001" should be 16.5 EUR
    And the charger "1" at location "LOC-001" should be available again
