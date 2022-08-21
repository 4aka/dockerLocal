package com.atqc.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Driver {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        if (Config.platform == Platform.DOCKER_LOCAL_CHROME) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("chrome.switches", "--disable-extensions");
            options.addArguments("start-maximized");
            options.addArguments("disable-popup-blocking");
            options.addArguments("no-proxy-server");

            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver(options));
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void closeDriver() {
        driver.get().quit();
    }
}
