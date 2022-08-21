package com.atqc.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.atqc.framework.Common.fakeRandomIntFromTo;
import static com.atqc.framework.Driver.getDriver;
import static com.atqc.framework.Wait.waitUntilClickable;

public class BasePage {

    public BasePage() {
        PageFactory.initElements(getDriver(), this);
    }

    @Step("Select random element from the dropdown")
    public String selectRandomElementFromTheDropDown(WebElement dropDown) {
        waitUntilClickable(dropDown).click();
        waitUntilClickable(getDriver().findElement(By.xpath("//ul/li[last()]")));
        List<WebElement> list = getDriver().findElements(By.xpath("//ul/li"));
        int random = fakeRandomIntFromTo(2, list.size());
        WebElement element = getDriver().findElement(By.xpath("//ul/li[" + random + "]"));
        scrollToElement(element);
        waitUntilClickable(element).click();
        return element.getText().isEmpty() || element.getText().equals("0") ? element.getAttribute("value") : element.getText();
    }

    public static void click(WebElement continueButton) {
        waitUntilClickable(continueButton, 20).click();
    }

    public static void scrollToElement(WebElement element){
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @Step("Select dropdown element")
    static void selectDropdownValue(WebElement dropdown, String elementValue) {
        waitUntilClickable(dropdown).click();
        waitUntilClickable(getDriver().findElement(By.xpath("//ul/li[last()]")));
        List<WebElement> list = getDriver().findElements(By.xpath("//ul/li"));
        list.stream().filter(it -> it.getText().equalsIgnoreCase(elementValue)).findFirst().get().click();
    }
}
