Feature: Browse Equipment

  Scenario: Farmer can view available equipment

    Given the Namma Yantra application is open
    When the farmer enters valid registration details
    And clicks the Farmer role
    Then all available machines should be displayed