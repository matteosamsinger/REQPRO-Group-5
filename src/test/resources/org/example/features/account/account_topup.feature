Feature: Top up account
  As a customer
  I want to top up my prepaid balance
  So that I can pay for charging

  Background:
    Given an empty charging network
    And an account with id "A-001" name "Alice" and email "alice@example.com" exists

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Top up increases the balance
    When I top up account "A-001" by 50 EUR
    Then the account "A-001" should have balance 50 EUR
    And the account "A-001" should have 1 top-up

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Multiple top-ups accumulate
    When I top up account "A-001" by 20 EUR
    And I top up account "A-001" by 0.01 EUR
    Then the account "A-001" should have balance 20.01 EUR
    And the account "A-001" should have 2 top-up

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Top up with zero amount fails
    When I top up account "A-001" by -10 EUR
    Then I should get an error "Top-up amount must be > 0"
    And the account "A-001" should have balance 0 EUR
    And the account "A-001" should have 0 top-up