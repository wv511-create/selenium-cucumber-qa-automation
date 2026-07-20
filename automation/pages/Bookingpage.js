const BasePage = require("./BasePage");
const { By, until } = require("selenium-webdriver");

class BookingPage extends BasePage {

    constructor(driver) {
        super(driver);

        this.bookingTitle = By.css("#content-area h2");
        this.confirmButton = By.xpath("//button[contains(text(),'Confirm Booking')]");
        this.totalCost = By.xpath("//*[contains(text(),'Total Cost')]");
        this.bookingConfirmation = By.xpath("//*[contains(text(),'Call Owner')]");
    }

    async isBookingPageDisplayed() {
        let text = '';
        await this.driver.wait(async () => {
            try {
                const heading = await this.driver.findElement(this.bookingTitle);
                text = await this.driver.executeScript("return arguments[0].textContent;", heading);
                return text.includes("Book");
            } catch (e) {
                return false;
            }
        }, 10000);
        return text.includes("Book");
    }

    async clickConfirmBooking() {
        await this.driver.findElement(this.confirmButton).click();

        const alert = await this.driver.wait(until.alertIsPresent(), 5000);
        const alertText = await alert.getText();
        console.log("Alert text:", alertText);
        await alert.accept();
    }

    async isTotalCostDisplayed() {
        const cost = await this.driver.wait(until.elementLocated(this.totalCost), 5000);
        return await cost.isDisplayed();
    }

    async isMyBookingsDisplayed() {
        let found = false;
        await this.driver.wait(async () => {
            try {
                const el = await this.driver.findElement(this.bookingConfirmation);
                found = await el.isDisplayed();
                return found;
            } catch (e) {
                return false;
            }
        }, 10000);
        return found;
    }
}

module.exports = BookingPage;