const { Given, When, Then, Before, After } = require("@cucumber/cucumber");
const assert = require("assert");

const getDriver = require("../utils/driver");
const RegistrationPage = require("../pages/RegistrationPage");
const BrowseEquipmentPage = require("../pages/BrowseEquipmentPage");
const BookingPage = require("../pages/BookingPage");
const MyBookingsPage = require("../pages/MyBookingsPage");
const config = require("../config/config");

let driver;
let registrationPage;
let browsePage;
let bookingPage;
let myBookingsPage;

Before(async function () {
    driver = await getDriver();

    registrationPage = new RegistrationPage(driver);
    browsePage = new BrowseEquipmentPage(driver);
    bookingPage = new BookingPage(driver);
    myBookingsPage = new MyBookingsPage(driver);
});

After(async function () {
    if (driver) {
        await driver.quit();
    }
});

Given("the Namma Yantra application is open", async function () {
    await registrationPage.open(config.baseUrl);
});

When("the farmer enters valid registration details", async function () {
    await registrationPage.enterName("Ani V");
    await registrationPage.enterPhone("9876543210");
    await registrationPage.enterVillage("Bengaluru");
});

When("clicks the Farmer role", async function () {
    await registrationPage.clickFarmer();
});

Then("the Farmer Dashboard should be displayed", async function () {
    const machineCount = await browsePage.getMachineCount();
    assert.ok(machineCount > 0, "Farmer dashboard did not load.");
});

Then("all available machines should be displayed", async function () {
    const machineCount = await browsePage.getMachineCount();
    assert.strictEqual(machineCount, 6, `Expected 6 machines but found ${machineCount}`);
});

When("the farmer selects an available machine", async function () {
    await browsePage.clickFirstMachine();
    await driver.sleep(2000);
});

Then("the booking page should be displayed", async function () {
    const displayed = await bookingPage.isBookingPageDisplayed();
    assert.strictEqual(displayed, true);
});

When("the farmer confirms the booking", async function () {
    await bookingPage.clickConfirmBooking();
});

Then("the booking should be successful", async function () {
    const displayed = await bookingPage.isMyBookingsDisplayed();
    assert.strictEqual(displayed, true, "Booking confirmation screen was not displayed.");
});

Then("the booked machine should appear in My Bookings", async function () {

    const displayed = await myBookingsPage.isDisplayed();
    assert.strictEqual(displayed, true);

    const status = await myBookingsPage.getStatus();
    assert.strictEqual(status, "Pending");


});

When("the farmer clicks the Farmer role without entering any details", async function () {
    await registrationPage.clickFarmer();
});

Then("an error message should be displayed", async function () {
    const displayed = await registrationPage.isErrorDisplayed();
    assert.strictEqual(displayed, true, "Expected an error message for empty registration fields.");
});