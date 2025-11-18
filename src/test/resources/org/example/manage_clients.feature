Feature: Manage clients
  As a client
  I want to have an account
  So that I can pay for charging sessions

  Scenario: Register a new client with an account
    Given an empty charging network
    When I register a client with id "C-001" name "Alice" and email "alice@example.com"
    Then there should be a client with id "C-001" and name "Alice"
    And the client "C-001" should have an account with balance 0 EUR

  Scenario: Top up a client account
    Given an empty charging network
    And a client with id "C-002" name "Bob" and email "bob@example.com" and an empty account exists
    When I top up the account of client "C-002" by 50 EUR
    Then the account of client "C-002" should have a balance of 50 EUR
