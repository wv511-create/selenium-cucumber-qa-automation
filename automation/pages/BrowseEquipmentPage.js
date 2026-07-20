const BasePage = require("./BasePage");
const { By } = require("selenium-webdriver");

class BrowseEquipmentPage extends BasePage {

    constructor(driver) {
        super(driver);

        this.machineCards = By.css(".card[onclick]");
        this.machineNames = By.css(".card div");
        this.bookButtons = By.css(".btn");
    }

    async getMachineCount() {
        const cards = await this.driver.findElements(this.machineCards);
        return cards.length;
    }

    async getBookButtonCount() {
        const buttons = await this.driver.findElements(this.bookButtons);
        return buttons.length;
    }

async clickFirstMachine() {
    const cards = await this.driver.findElements(this.machineCards);
    await cards[0].click();
}

}

module.exports = BrowseEquipmentPage;