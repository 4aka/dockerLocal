package com.atqc.backend;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    public RequestSpecification REQUEST_SPEC =
            new RequestSpecBuilder()
                    .setContentType("application/json")
                    .build();

    @BeforeSuite
    public void addFilters() {

        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
