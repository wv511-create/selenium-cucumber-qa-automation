Feature: My Bookings

Scenario: Farmer views booked machine
  Given the Namma Yantra application is open
  When the farmer enters valid registration details
  And clicks the Farmer role
  And the farmer selects an available machine
  And the farmer confirms the booking
  Then the booked machine should appear in My Bookings