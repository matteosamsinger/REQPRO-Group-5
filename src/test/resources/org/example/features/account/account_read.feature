Feature: Read account
  As a customer or operator
  I want to read account data
  So that I can verify account details

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Read an existing account
    Given an account with id "A-001" name "Alice" and email "alice@example.com" exists
    When I read the account "A-001"
    Then I should see account id "A-001" name "Alice" and email "alice@example.com"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Read an account with minimal strings
    Given an account with id "A-1" name "A" and email "a@b.c" exists
    When I read the account "A-1"
    Then I should see account id "A-1" name "A" and email "a@b.c"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Reading an unknown account fails
    When I read the account "A-404"
    Then I should get an error "Account not found: A-404"