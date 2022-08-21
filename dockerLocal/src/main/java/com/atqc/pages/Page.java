package com.atqc.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.atqc.framework.Wait.waitUntilClickable;

@Getter
public class Page extends BasePage {

    @FindBy(id = "search_form_input_homepage")
    private WebElement searchField;

    @FindBy(id = "search_button_homepage")
    private WebElement searchButton;

    @FindBy(id = "search_form_input")
    public WebElement newSearchField;

    public void search(String data) {
        waitUntilClickable(searchField).sendKeys(data);
        waitUntilClickable(searchButton).click();
    }
}
