const { Builder } = require("selenium-webdriver");
const chrome = require("selenium-webdriver/chrome");
const config = require("../config/config");

require("chromedriver");

async function getDriver() {
    const options = new chrome.Options();

    if (process.env.CI === "true") {
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
    }

    return await new Builder()
        .forBrowser(config.browser)
        .setChromeOptions(options)
        .build();
}

module.exports = getDriver;