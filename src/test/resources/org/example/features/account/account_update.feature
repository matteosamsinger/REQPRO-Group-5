Feature: Update account
  As a customer
  I want to update my account data
  So that my details stay correct

  Background:
    Given an empty charging network

  # -------------------------
  # Happy Path
  # -------------------------
  Scenario: Update account name and email
    Given an account with id "A-001" name "Alice" and email "alice@example.com" exists
    When I update account "A-001" name to "Alicia" and email to "alicia@example.com"
    Then I should see account id "A-001" name "Alicia" and email "alicia@example.com"

  # -------------------------
  # Edge Case
  # -------------------------
  Scenario: Update account with the same values (no change)
    Given an account with id "A-001" name "Alice" and email "alice@example.com" exists
    When I update account "A-001" name to "Alice" and email to "alice@example.com"
    Then I should see account id "A-001" name "Alice" and email "alice@example.com"

  # -------------------------
  # Error Case
  # -------------------------
  Scenario: Updating an unknown account fails
    When I update account "A-404" name to "Ghost" and email to "ghost@example.com"
    Then I should get an error "Account not found: A-404"