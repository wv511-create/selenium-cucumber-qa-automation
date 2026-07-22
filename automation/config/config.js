module.exports = {
    baseUrl: process.env.BASE_URL || "http://localhost:8000",
    browser: process.env.BROWSER || "chrome",
    timeout: Number(process.env.TIMEOUT) || 10000
};