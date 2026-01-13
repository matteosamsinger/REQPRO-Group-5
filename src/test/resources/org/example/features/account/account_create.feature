Feature: Create account
  As a customer
  I want to register an account
  So that I can use the charging network

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Create a new account successfully
    When I create an account with id "A-001" name "Alice" and email "alice@example.com"
    Then there should be an account with id "A-001" and name "Alice"
    And the account "A-001" should have balance 0 EUR

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Create an account with minimal data
    When I create an account with id "A-1" name "A" and email "a@b.at"
    Then there should be an account with id "A-1" and name "A"
    And the account "A-1" should have balance 0 EUR

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Creating an account with an existing id fails
    Given an account with id "A-009" name "Old" and email "old@example.com" exists
    When I create an account with id "A-009" name "New" and email "new@example.com"
    Then I should get an error "Account already exists: A-009"
    And there should be an account with id "A-009" and name "Old"