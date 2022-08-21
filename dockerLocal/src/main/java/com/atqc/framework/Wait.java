package com.atqc.framework;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.atqc.framework.Driver.getDriver;

@Log4j2
public class Wait {

    public static WebElement waitUntilClickable(WebElement element, int... seconds) {
        int waitTime = seconds.length==0 ? 30 : seconds[0];
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(waitTime))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    public static WebElement waitUntilElementVisible(WebElement element, int... seconds) {
        int waitTime = seconds.length == 0 ? 30 : seconds[0];
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(waitTime))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    public static boolean waitUntilVisible(WebElement element, int... seconds) {
        int waitTime = seconds.length==0 ? 30 : seconds[0];
        try {
            new FluentWait<>(getDriver())
                    .withTimeout(Duration.ofSeconds(waitTime))
                    .pollingEvery(Duration.ofMillis(250))
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            log.error("WebElement " + element + " is not visible");
            return false;
        }
        return true;
    }

    public static void waitUntilPresent(By by) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(20), Duration.ofMillis(200))
                .until((ExpectedCondition<Boolean>) d -> {
                    try {
                        getDriver().findElement(by);
                        return true;
                    } catch (NoSuchElementException | StaleElementReferenceException e) {
                        return false;
                    }
                });
    }

    public static void waitUntilNotVisible(WebElement element, int... seconds) {
        int waitTime = seconds.length==0 ? 30 : seconds[0];
        try {
            new FluentWait<>(getDriver())
                    .withTimeout(Duration.ofSeconds(waitTime))
                    .pollingEvery(Duration.ofMillis(100))
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                    .until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            log.error("WebElement " + element + " is not visible");
        }
    }

    public static void pressHomeButton() {
        Actions builder = new Actions(getDriver());
        builder.sendKeys(Keys.HOME).build().perform();
    }
}
