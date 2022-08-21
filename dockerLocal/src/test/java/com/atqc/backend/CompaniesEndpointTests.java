package com.atqc.backend;

import com.atqc.framework.Common;
import com.atqc.models.CompanyModel;
import com.atqc.models.StatsModel;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@Log4j2
public class CompaniesEndpointTests extends BaseTest{

    String companies = "";

    @DataProvider(name = "data-provider")
    public Object[][] companiesList() {
        return new Object[][]{
                {"AAPL"},
                {"MSFT"},
                {"AMZN"},
                {"TSLA"},
                {"GOOGL"},
                {"GOOG"},
                {"NVDA"},
                {"BRK.B"},
                {"META"},
                {"UNH"}
        };
    }

    @Test(testName = "GET /companies with wrong data returns 404, Not found")
    public void getCompaniesReturnsUnknownSymbol() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(companies + Common.fakeFixedString(10))
                .then()
                .statusCode(404)
                .body("detail", containsString("Not Found"));
    }

    @Test(testName = "Check /companies returns list of symbols", dataProvider = "data-provider")
    public void checkCompaniesReturnsListOfSymbols(String symbol) {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(companies)
                .then()
                .statusCode(200)
                .body(containsString(symbol));
    }

    @Test(testName = "Logo link is present in the response", dataProvider = "data-provider")
    public void logoLinkIsPresentInTheResponse(String companySymbol) {

        CompanyModel company = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(companies + companySymbol)
                .then()
                .statusCode(200)
                .extract()
                .as(CompanyModel.class);

        Assert.assertTrue(company.getLogoLink().startsWith("https://"));
        Assert.assertTrue(company.getLogoLink().endsWith(".png"));
    }

    @SneakyThrows
    @Test(testName = "Check response contains list of companies", dataProvider = "data-provider")
    public void checkResponseContainsListOfCompanies(String symbol) {
        CompanyModel oaiCompanyModel = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(companies + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(CompanyModel.class);

        CompanyModel iexCompanyModel = given()
                .spec(REQUEST_SPEC)
                .queryParam("token", "pk_c17b30da5c2c497ba300cf50fcdd8da9")
                .when()
                .get("https://cloud.iexapis.com/stable/stock/" + symbol + "/company")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(CompanyModel.class);

        Assert.assertEquals(oaiCompanyModel.getSymbol(), iexCompanyModel.getSymbol());
        Assert.assertEquals(oaiCompanyModel.getCompanyName(), iexCompanyModel.getCompanyName());
        Assert.assertEquals(oaiCompanyModel.getIndustry(), iexCompanyModel.getIndustry());
        Assert.assertEquals(oaiCompanyModel.getEmployees(), iexCompanyModel.getEmployees());
        Assert.assertEquals(oaiCompanyModel.getCEO(), iexCompanyModel.getCEO());
        Assert.assertEquals(oaiCompanyModel.getExchange(), iexCompanyModel.getExchange());
        Assert.assertEquals(oaiCompanyModel.getIssueType(), iexCompanyModel.getIssueType());
        Assert.assertEquals(oaiCompanyModel.getSector(), iexCompanyModel.getSector());
        Assert.assertEquals(oaiCompanyModel.getSecurityName(), iexCompanyModel.getSecurityName());
        Assert.assertEquals(oaiCompanyModel.getDividendYield(), iexCompanyModel.getDividendYield());
        Assert.assertEquals(oaiCompanyModel.getMarketCapitalization(), iexCompanyModel.getMarketCapitalization());
        Assert.assertEquals(oaiCompanyModel.getDescription(), iexCompanyModel.getDescription());
        Assert.assertEquals(oaiCompanyModel.getAddress(), iexCompanyModel.getAddress());
        Assert.assertEquals(oaiCompanyModel.getAddress2(), iexCompanyModel.getAddress2());
        Assert.assertEquals(oaiCompanyModel.getState(), iexCompanyModel.getState());
        Assert.assertEquals(oaiCompanyModel.getCity(), iexCompanyModel.getCity());
        Assert.assertEquals(oaiCompanyModel.getZip(), iexCompanyModel.getZip());
        Assert.assertEquals(oaiCompanyModel.getCountry(), iexCompanyModel.getCountry());
        Assert.assertEquals(oaiCompanyModel.getPhone(), iexCompanyModel.getPhone());
        Assert.assertEquals(oaiCompanyModel.getWebsite(), iexCompanyModel.getWebsite());
        Assert.assertEquals(oaiCompanyModel.getPrimarySicCode(), iexCompanyModel.getPrimarySicCode());
        Assert.assertEquals(oaiCompanyModel.getTags(), iexCompanyModel.getTags());
    }

    @SneakyThrows
    @Test(testName = "Check company.stats are equaled to iex.stats", dataProvider = "data-provider")
    public void checkStatsAreEqualed(String symbol) {
        CompanyModel oaiCompanyModel = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(companies + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(CompanyModel.class);

        StatsModel oaiStats = oaiCompanyModel.getStats();

        StatsModel iexStats = given()
                .spec(REQUEST_SPEC)
                .queryParam("token", "pk_c17b30da5c2c497ba300cf50fcdd8da9")
                .when()
                .get("https://cloud.iexapis.com/stable/stock/" + symbol + "/stats")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(StatsModel.class);

        Assert.assertEquals(oaiStats.getCompanyName(), iexStats.getCompanyName());
        Assert.assertEquals(oaiStats.getMarketcap(), iexStats.getMarketcap());
        Assert.assertEquals(oaiStats.getWeek52high(), iexStats.getWeek52high());
        Assert.assertEquals(oaiStats.getWeek52low(), iexStats.getWeek52low());
        Assert.assertEquals(oaiStats.getWeek52highSplitAdjustOnly(), iexStats.getWeek52highSplitAdjustOnly());
        Assert.assertEquals(oaiStats.getWeek52lowSplitAdjustOnly(), iexStats.getWeek52lowSplitAdjustOnly());
        Assert.assertEquals(oaiStats.getWeek52change(), iexStats.getWeek52change());
        Assert.assertEquals(oaiStats.getSharesOutstanding(), iexStats.getSharesOutstanding());
        Assert.assertEquals(oaiStats.getAvg10Volume(), iexStats.getAvg10Volume());
        Assert.assertEquals(oaiStats.getAvg30Volume(), iexStats.getAvg30Volume());
        Assert.assertEquals(oaiStats.getDay200MovingAvg(), iexStats.getDay200MovingAvg());
        Assert.assertEquals(oaiStats.getDay50MovingAvg(), iexStats.getDay50MovingAvg());
        Assert.assertEquals(oaiStats.getEmployees(), iexStats.getEmployees());
        Assert.assertEquals(oaiStats.getTtmDividendRate(), iexStats.getTtmDividendRate());
        Assert.assertEquals(oaiStats.getDividendYield(), iexStats.getDividendYield());
        Assert.assertEquals(oaiStats.getNextDividendDate(), iexStats.getNextDividendDate());
        Assert.assertEquals(oaiStats.getExDividendDate(), iexStats.getExDividendDate());
        Assert.assertEquals(oaiStats.getPeRatio(), iexStats.getPeRatio());
        Assert.assertEquals(oaiStats.getBeta(), iexStats.getBeta());
        Assert.assertEquals(oaiStats.getMaxChangePercent(), iexStats.getMaxChangePercent());
        Assert.assertEquals(oaiStats.getYear5ChangePercent(), iexStats.getYear5ChangePercent());
        Assert.assertEquals(oaiStats.getYear2ChangePercent(), iexStats.getYear2ChangePercent());
        Assert.assertEquals(oaiStats.getYear1ChangePercent(), iexStats.getYear1ChangePercent());
        Assert.assertEquals(oaiStats.getYtdChangePercent(), iexStats.getYtdChangePercent());
        Assert.assertEquals(oaiStats.getMonth6ChangePercent(), iexStats.getMonth6ChangePercent());
        Assert.assertEquals(oaiStats.getMonth3ChangePercent(), iexStats.getMonth3ChangePercent());
        Assert.assertEquals(oaiStats.getMonth1ChangePercent(), iexStats.getMonth1ChangePercent());
        Assert.assertEquals(oaiStats.getDay30ChangePercent(), iexStats.getDay30ChangePercent());
        Assert.assertEquals(oaiStats.getDay5ChangePercent(), iexStats.getDay5ChangePercent());
        Assert.assertEquals(oaiStats.getTtmEPS(), iexStats.getTtmEPS());
    }

}
