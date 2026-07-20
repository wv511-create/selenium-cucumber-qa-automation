Feature: Registration validation

  Scenario: Registration fails when required fields are empty
    Given the Namma Yantra application is open
    When the farmer clicks the Farmer role without entering any details
    Then an error message should be displayed