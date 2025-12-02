Feature: Invoice status
  As an owner
  I want to view the invoice status of a client
  So that I can see all top-ups, charging transactions and the current balance

  Scenario: Invoice shows top-ups, charging transactions and current balance
    Given an empty charging network
    And a client with id "CLI-001" name "Aulona" and an account with balance 0 EUR exists
    When I top up the account of client "CLI-001" by 50 EUR
    And I top up the account of client "CLI-001" by 20 EUR
    And I add a charging transaction of 30 EUR for client "CLI-001"
    And I request the invoice status for client "CLI-001"
    Then the invoice for client "CLI-001" should contain 2 top-ups and 1 charging transaction
    And the account of client "CLI-001" should have a balance of 40 EUR
