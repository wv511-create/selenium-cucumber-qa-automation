const BasePage = require("./BasePage");
const { By } = require("selenium-webdriver");

class RegistrationPage extends BasePage {

    constructor(driver) {
        super(driver);

        this.name = By.id("reg-name");
        this.phone = By.id("reg-phone");
        this.village = By.id("reg-village");

        this.farmerButton = By.xpath("//div[@class='role-card' and @onclick=\"setRole('Farmer')\"]");

        this.error = By.id("reg-error");
    }

    async enterName(name) {
        await this.driver.findElement(this.name).sendKeys(name);
    }

    async enterPhone(phone) {
        await this.driver.findElement(this.phone).sendKeys(phone);
    }

    async enterVillage(village) {
        await this.driver.findElement(this.village).sendKeys(village);
    }

    async clickFarmer() {
        await this.driver.findElement(this.farmerButton).click();
    }

    async isErrorDisplayed() {
        return await this.driver.findElement(this.error).isDisplayed();
    }

}

module.exports = RegistrationPage;