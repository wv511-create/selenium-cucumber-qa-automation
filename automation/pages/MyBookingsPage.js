const BasePage = require("./BasePage");
const { By, until } = require("selenium-webdriver");

class MyBookingsPage extends BasePage {

    constructor(driver) {
        super(driver);

        // No "My Bookings" h1 exists on this screen — use the booking card
        // (identified by "Call Owner", which only appears once a real booking exists)
        // as the signal that this screen is displayed.
        this.bookingCard = By.xpath("//*[contains(text(),'Call Owner')]/ancestor::div[contains(@class,'card')]");
        this.machineName = By.xpath("//*[contains(text(),'Call Owner')]/ancestor::div[contains(@class,'card')]//div[1]");
        this.statusInCard = By.xpath("//*[contains(text(),'Call Owner')]/ancestor::div[contains(@class,'card')]//*[contains(text(),'Pending')]");
    }

    async isDisplayed() {
        let found = false;
        await this.driver.wait(async () => {
            try {
                const el = await this.driver.findElement(this.bookingCard);
                found = await el.isDisplayed();
                return found;
            } catch (e) {
                return false;
            }
        }, 10000);
        return found;
    }

    async getStatus() {
        const status = await this.driver.findElement(this.statusInCard);
        return await status.getText();
    }
}

module.exports = MyBookingsPage;