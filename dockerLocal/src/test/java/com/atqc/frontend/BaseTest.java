package com.atqc.frontend;

import com.atqc.framework.Driver;
import com.atqc.framework.DriverActions;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.atqc.framework.Config.baseUrl;
import static com.atqc.framework.Driver.closeDriver;

@Log4j2
public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        Driver.initDriver();
        DriverActions.open(baseUrl);


    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {

        closeDriver();
    }
}
