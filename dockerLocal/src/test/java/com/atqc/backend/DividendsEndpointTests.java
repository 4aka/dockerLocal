package com.atqc.backend;

import com.atqc.framework.Common;
import com.atqc.models.DividendModel;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.atqc.framework.Common.mapper;
import static com.atqc.framework.Common.replace;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DividendsEndpointTests extends BaseTest {

    String dividends = "";

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

    @Test(testName = "Check 404 code when symbol does not exist")
    public void checkDividendsReturns404() {
        String symbol = Common.fakeFixedString(10);

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends + symbol)
                .then()
                .log().ifError()
                .statusCode(404)
                .body("detail", containsString("Could not find dividend for " + symbol));
    }

    @Test(testName = "Check /dividends returns 200 code with AAPL symbol")
    public void checkDividendsReturns200() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends + "AAPL")
                .then()
                .log().ifError()
                .statusCode(200);
    }

    @Test(testName = "Check /dividends returns 422 code")
    public void checkDividendsReturns422Code() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends)
                .then()
                .statusCode(422)
                .body("detail", containsString("Please provide at least one symbol"));
    }

    @SneakyThrows
    @Test(testName = "Check response has symbol, dividends fields", dataProvider = "data-provider")
    public void checkResponseHaveCertainFields(String symbol) {

        DividendModel dividendList = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(DividendModel.class);

        if (dividendList.getDividends().isEmpty()) {
            Assert.fail("=============== Dividends are empty ===============");
        }

        Assert.assertFalse(dividendList.getSymbol().isEmpty());
        Assert.assertFalse(dividendList.getDividends().isEmpty());

    }

    @SneakyThrows
    @Test(testName = "Check 'dividends' has range 1 year")
    public void checkDividendsHasRange1Year() {

        DividendModel model = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends + "AAPL")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(DividendModel.class);

        List<DividendModel> dividendList = mapper()
                .readValue(replace(model.getDividends()), new TypeReference<List<DividendModel>>(){});

        Assert.assertEquals(dividendList.size(), 4);
    }

    @SneakyThrows
    @Test(testName = "Check fields are not null")
    public void checkDividendsHasCertainFields() {
        DividendModel model = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends + "AAPL")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(DividendModel.class);

        List<DividendModel> dividendList = mapper()
                .readValue(replace(model.getDividends()), new TypeReference<List<DividendModel>>(){});

        for (DividendModel dividend : dividendList) {
            Assert.assertTrue(dividend.getAmount() > 0);
            Assert.assertFalse(dividend.getCurrency().isEmpty());
            Assert.assertFalse(dividend.getDeclaredDate().isEmpty());
            Assert.assertFalse(dividend.getDescription().isEmpty());
            Assert.assertFalse(dividend.getExDate().isEmpty());
            Assert.assertFalse(dividend.getFlag().isEmpty());
            Assert.assertFalse(dividend.getFrequency().isEmpty());
            Assert.assertFalse(dividend.getPaymentDate().isEmpty());
            Assert.assertFalse(dividend.getRecordDate().isEmpty());
            Assert.assertTrue(dividend.getRefid() > 0);
            Assert.assertFalse(dividend.getSymbol().isEmpty());
            Assert.assertFalse(dividend.getId().isEmpty());
            Assert.assertFalse(dividend.getKey().isEmpty());
            Assert.assertFalse(dividend.getSubkey().isEmpty());
            Assert.assertTrue(dividend.getDate() > 0);
            Assert.assertTrue(dividend.getUpdated() > 0);
        }
    }

    @SneakyThrows
    @Test(testName = "Check response contains list of dividends")
    public void checkResponseContainsListOfDividends() {
        String url = dividends.replace("dividends/", "dividends");
        String symbol1 = "AAPL";
        String symbol2 = "MSFT";

        DividendModel[] model = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", symbol1)
                .queryParam("symbol", symbol2)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(DividendModel[].class);

        List<DividendModel> dividendList = new ArrayList<>();

        for (DividendModel dividendsModel : model) {
            dividendList
                    .addAll(mapper()
                    .readValue(replace(dividendsModel.getDividends()),
                            new TypeReference<List<DividendModel>>() {}));
        }

        for (DividendModel dividend : dividendList) {
            Assert.assertTrue(dividend.getAmount() > 0);
            Assert.assertFalse(dividend.getCurrency().isEmpty());
            Assert.assertFalse(dividend.getDeclaredDate().isEmpty());
            Assert.assertFalse(dividend.getDescription().isEmpty());
            Assert.assertFalse(dividend.getExDate().isEmpty());
            Assert.assertFalse(dividend.getFlag().isEmpty());
            Assert.assertFalse(dividend.getFrequency().isEmpty());
            Assert.assertFalse(dividend.getPaymentDate().isEmpty());
            Assert.assertFalse(dividend.getRecordDate().isEmpty());
            Assert.assertTrue(dividend.getRefid() > 0);
            Assert.assertFalse(dividend.getSymbol().isEmpty());
            Assert.assertFalse(dividend.getId().isEmpty());
            Assert.assertFalse(dividend.getKey().isEmpty());
            Assert.assertFalse(dividend.getSubkey().isEmpty());
            Assert.assertTrue(dividend.getDate() > 0);
            Assert.assertTrue(dividend.getUpdated() > 0);
        }
    }

    @SneakyThrows
    @Test(testName = "/dividends returns with 1 year range", dataProvider = "data-provider")
    public void dividendsReturnsWith1YearRange(String symbol) {
        DividendModel oaiModel = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(dividends + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(DividendModel.class);

        if (oaiModel.getDividends().isEmpty()) {
            Assert.fail("=============== Dividends are empty ===============");
        }
        List<DividendModel> oaiDividendList = mapper()
                .readValue(replace(oaiModel.getDividends()), new TypeReference<List<DividendModel>>(){});

        DividendModel[] iexModel = given()
                .spec(REQUEST_SPEC)
                .queryParam("token", "pk_c17b30da5c2c497ba300cf50fcdd8da9")
                .when()
                .get("https://cloud.iexapis.com/stable/stock/" + symbol + "/dividends/1y")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(DividendModel[].class);

        for (int i = 0; i < iexModel.length; i++) {
            Assert.assertEquals(oaiDividendList.get(i).getAmount(), iexModel[i].getAmount());
            Assert.assertEquals(oaiDividendList.get(i).getCurrency(), iexModel[i].getCurrency());
            Assert.assertEquals(oaiDividendList.get(i).getDeclaredDate(), iexModel[i].getDeclaredDate());
            Assert.assertEquals(oaiDividendList.get(i).getDescription(), iexModel[i].getDescription());
            Assert.assertEquals(oaiDividendList.get(i).getExDate(), iexModel[i].getExDate());
            Assert.assertEquals(oaiDividendList.get(i).getFlag(), iexModel[i].getFlag());
            Assert.assertEquals(oaiDividendList.get(i).getFrequency(), iexModel[i].getFrequency());
            Assert.assertEquals(oaiDividendList.get(i).getPaymentDate(), iexModel[i].getPaymentDate());
            Assert.assertEquals(oaiDividendList.get(i).getRecordDate(), iexModel[i].getRecordDate());
            Assert.assertEquals(oaiDividendList.get(i).getRefid(), iexModel[i].getRefid());
            Assert.assertEquals(oaiDividendList.get(i).getSymbol(), iexModel[i].getSymbol());
            Assert.assertEquals(oaiDividendList.get(i).getId(), iexModel[i].getId());
            Assert.assertEquals(oaiDividendList.get(i).getKey(), iexModel[i].getKey());
            Assert.assertEquals(oaiDividendList.get(i).getSubkey(), iexModel[i].getSubkey());
            Assert.assertEquals(oaiDividendList.get(i).getDate(), iexModel[i].getDate());
            Assert.assertEquals(oaiDividendList.get(i).getUpdated(), iexModel[i].getUpdated());
        }
    }
}
