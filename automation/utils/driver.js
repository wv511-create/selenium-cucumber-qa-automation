const { Builder } = require("selenium-webdriver");
require("chromedriver");

async function getDriver() {
    return await new Builder()
        .forBrowser("chrome")
        .build();
}

module.exports = getDriver;