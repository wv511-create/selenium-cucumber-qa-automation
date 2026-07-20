Feature: Booking Equipment

Scenario: Farmer books available equipment
  Given the Namma Yantra application is open
  When the farmer enters valid registration details
  And clicks the Farmer role
  And the farmer selects an available machine
  Then the booking page should be displayed
  When the farmer confirms the booking
  Then the booking should be successful