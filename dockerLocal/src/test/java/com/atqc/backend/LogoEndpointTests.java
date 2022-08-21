package com.atqc.backend;

import com.atqc.framework.Common;
import com.atqc.models.LogoModel;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@Log4j2
public class LogoEndpointTests extends BaseTest{

    String logos = "";

    @DataProvider(name = "data-provider")
    public Object[][] symbols() {
        return new Object[][]{
                {"AAPL"},
                {"MSFT"},
                {"AMZN"},
                {"TSLA"},
                {"GOOGL"},
                {"GOOG"},
                {"NVDA"},
                {"META"},
                {"UNH"},
                {"BRK.B"},
                {"NI-B"},
        };
    }

    @Test(testName = "/logos with wrong data returns 404, Not found")
    public void getLogosReturnsUnknownSymbol() {
        String logo = Common.fakeFixedString(10);

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos + logo)
                .then()
                .log().ifError()
                .statusCode(404)
                .body("detail", containsString("Could not find logo for " + logo));
    }

    @Test(testName = "/logos with empty symbol returns 422")
    public void logosReturns422WithEmptySymbol() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos)
                .then()
                .statusCode(422)
                .body("detail", containsString("Please provide symbol for getting logo!"));
    }

    @Test(testName = "/logos returns code 200")
    public void logoLinkReturns200Code() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos + "AAPL")
                .then()
                .statusCode(200);
    }

    @Test(testName = "Logo returns correct model")
    public void logoReturnsCorrectModel() {

        LogoModel logoModel = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos + "AAPL")
                .then()
                .statusCode(200)
                .extract()
                .as(LogoModel.class);

        Assert.assertFalse(logoModel.getSymbol().isEmpty());
        Assert.assertFalse(logoModel.getLogoLink().isEmpty());
    }

    @Test(testName = "Logo link is present in the response", dataProvider = "data-provider")
    public void logoLinkIsPresentInTheResponse(String logosSymbol) {

        LogoModel logo = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos + logosSymbol)
                .then()
                .statusCode(200)
                .extract()
                .as(LogoModel.class);

        Assert.assertTrue(logo.getLogoLink().startsWith("https://"));
        Assert.assertTrue(logo.getLogoLink().endsWith(".png"));
    }

    @Test(testName = "Logo returns correct symbol", dataProvider = "data-provider")
    public void logoReturnsCorrectSymbol(String symbol) {

        LogoModel logoModel = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos + symbol)
                .then()
                .statusCode(200)
                .extract()
                .as(LogoModel.class);

        Assert.assertEquals(logoModel.getSymbol(), symbol);
    }

    @Test(testName = "Logo returns multiple models")
    public void logoReturnsMultipleModels() {
        String url = logos.replace("logos/", "logos");

        LogoModel[] logosList = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", "AAPL")
                .queryParam("symbol", "AA")
                .when()
                .get(url) // "?symbol=AAPL&symbol=META"
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(LogoModel[].class);

        Assert.assertFalse(logosList[0].getSymbol().isEmpty());
        Assert.assertFalse(logosList[0].getLogoLink().isEmpty());

        Assert.assertFalse(logosList[1].getSymbol().isEmpty());
        Assert.assertFalse(logosList[1].getLogoLink().isEmpty());
    }

    @Test(testName = "Logo returns one model, but request has two same symbols")
    public void logoReturnsOneModel() {
        String url = logos.replace("logos/", "logos");

        LogoModel[] logosList = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", "AA")
                .queryParam("symbol", "AA")
                .when()
                .get(url) // "?symbol=AAPL&symbol=META"
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(LogoModel[].class);

        Assert.assertEquals(1, logosList.length);
    }

    @Test(testName = "Logo returns two models, but request has two same symbol and 1 another")
    public void logoReturnsTwoModel() {
        String url = logos.replace("logos/", "logos");

        LogoModel[] logosList = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", "AA")
                .queryParam("symbol", "AA")
                .queryParam("symbol", "AAPL")
                .when()
                .get(url) // "?symbol=AAPL&symbol=META"
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(LogoModel[].class);

        Assert.assertEquals(2, logosList.length);
    }

    @Test(testName = "Logo returns list of logo")
    public void logoReturnsListOfLogos() {
        String url = logos.replace("logos/", "logos");

        LogoModel[] logosList = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", "AAPL")
                .queryParam("symbol", "AA")
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(LogoModel[].class);

        Assert.assertEquals(logosList.length, 2);
    }

    @Test(testName = "IEX /logos is equeld to OAI /logos")
    public void splitsReturnsWith1YearRange() {
        String symbol = "NVDA";

        LogoModel logoModel = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(logos + symbol)
                .then()
                .statusCode(200)
                .extract()
                .as(LogoModel.class);

        Response iexResponse = given()
                .spec(REQUEST_SPEC)
                .queryParam("token", "pk_c17b30da5c2c497ba300cf50fcdd8da9")
                .when()
                .get("https://cloud.iexapis.com/stable/stock/" + symbol + "/logo")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(logoModel.getLogoLink(), iexResponse.getBody().jsonPath().get("url"));
    }

}
