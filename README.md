# Selenium + Cucumber QA Automation

Test automation suite for **Namma-Yantra Share**, a peer-to-peer farm equipment
rental app I built. Farmers browse nearby machines, book by the hour or day,
and track requests; owners list equipment and manage bookings.

The `web-prototype/` app is a static HTML/CSS/JavaScript SPA that runs entirely
in the browser in local demo mode (in-memory state — no backend or Firebase
required to run or test it). This repo also includes an Android app scaffold
(`android/`) and this Selenium + Cucumber test suite (`automation/`).

## What's tested

The suite covers 4 scenarios, all passing, defined in `automation/features/`:

| Feature file | Scenario |
|---|---|
| `registrationValidation.feature` | Registration fails when required fields are empty |
| `farmer-registration.feature` | Farmer can view available equipment |
| `booking.feature` | Farmer books available equipment |
| `mybooking.feature` | Farmer views booked machine |

Coverage: registration validation (empty required fields rejected), equipment
browsing (dashboard loads and all 6 listings appear), booking flow (select a
machine, confirm booking, accept the native JS confirmation alert), and
booking confirmation (booked machine appears in My Bookings with Pending
status).

## Tech stack

**Web prototype** (`web-prototype/`) — static HTML5, CSS3, JavaScript, no
dependencies to serve.

**Test suite** (`automation/`) — Node.js, Selenium WebDriver, Cucumber.js
(BDD/Gherkin), Page Object Model.

- `@cucumber/cucumber` ^13.1.1
- `selenium-webdriver` ^4.46.0
- `chromedriver` ^151.0.0

Chrome must be installed; `chromedriver` is pulled in as an npm dependency.

## Run locally

```bash
# Terminal 1 — start the app (tests expect port 3000)
cd web-prototype
python -m http.server 3000

# Terminal 2 — run the tests
cd automation
npm install
npm test
```

Expected result: `4 scenarios (4 passed)`.

## Notes

A couple of real SPA-testing issues came up while building this:

- **Native JS alerts** — the confirmation dialog must be accepted before any
  subsequent WebDriver command, or later calls just hang waiting on the alert.
- **Staged rendering** — content appears asynchronously, so assertions use
  wait/retry polling rather than a single immediate `getText()` check.

  ## Author
  Wani

