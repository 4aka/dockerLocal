package com.atqc.framework;

import static com.atqc.framework.PropertyLoader.retrieveProperty;

public class Config {

    public static String restApiBaseUri = PropertyLoader.getProperty("restApiBaseUri");
    public static String registerUrl = PropertyLoader.getProperty("baseUrl") + "/register";
    public static String restApiBaseUriNew = PropertyLoader.getProperty("restApiBaseUriNew");
    public static String baseUrl = PropertyLoader.getProperty("baseUrl");
    public static Platform platform = Platform.valueOf(retrieveProperty("webdriver.platform"));
    public static String userEmail = PropertyLoader.getProperty("userEmail");
    public static String userPassword = PropertyLoader.getProperty("userPassword");
    public static String uatPassword = PropertyLoader.getProperty("uatPassword");
    public static String testUserEmail = PropertyLoader.getProperty("testUserEmail");
    public static String testUserPassword = PropertyLoader.getProperty("testUserPassword");
    public static boolean isDataPrintingEnabled = Boolean.parseBoolean(PropertyLoader.getProperty("isTestLoggingEnabled"));

    public static String dbHost = PropertyLoader.getProperty("dbHost");
    public static String dbUser = PropertyLoader.getProperty("dbUser");
    public static String dbPass = PropertyLoader.getProperty("dbPass");
    public static String dbName = PropertyLoader.getProperty("dbName");

}
