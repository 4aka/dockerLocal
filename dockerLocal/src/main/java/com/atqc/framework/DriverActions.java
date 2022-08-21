package com.atqc.framework;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DriverActions {

    public static void open(String url) {
        Driver.getDriver().get(url);
    }

}
