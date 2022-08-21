package com.atqc.backend;

import com.atqc.framework.Common;
import com.atqc.models.SplitsModel;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.atqc.framework.Common.mapper;
import static com.atqc.framework.Common.replace;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@Log4j2
public class SplitsEndpointTests extends BaseTest {

    String splits = "";

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

    @Test(testName = "/splits with wrong data returns 404, Not found")
    public void getSplitsReturnsUnknownSymbol() {
        String symbol = Common.fakeFixedString(10);

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits + symbol)
                .then()
                .log().ifError()
                .statusCode(404)
                .body("detail", containsString("Could not find split basic for " + symbol));
    }

    @Test(testName = "Check splits without symbol returns 422 code")
    public void checkSplitsWithoutSymbolReturns422() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits)
                .then()
                .statusCode(422)
                .body("detail", containsString("Please provide at least one symbol"));
    }

    @Test(testName = "/splits returns code 200")
    public void splitsReturns200Code() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits + "AAPL")
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    @Test(testName = "/splits returns list of models")
    public void splitsReturnsListOfModels() {
        String symbol = "AMZN";

        SplitsModel splitsResponse = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel.class);

        List<SplitsModel> splitsList = mapper()
                .readValue(replace(splitsResponse.getSplits()
                        .replace("None", "null")), new TypeReference<List<SplitsModel>>(){});

        Assert.assertEquals(splitsList.size(), 2);
    }

    @SneakyThrows
    @Test(testName = "/splits returns correct model")
    public void splitsReturnsCorrectModel() {
        String symbol = "AMZN";

        SplitsModel splitsResponse = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel.class);

        List<SplitsModel> splitsList = mapper()
                .readValue(replace(splitsResponse.getSplits()
                        .replace("None", "null")), new TypeReference<List<SplitsModel>>(){});

        Assert.assertNull(splitsList.get(0).getDeclaredDate());
        Assert.assertFalse(splitsList.get(0).getDescription().isEmpty());
        Assert.assertFalse(splitsList.get(0).getExDate().isEmpty());
        Assert.assertTrue(splitsList.get(0).getFromFactor() >= 0);
        Assert.assertTrue(splitsList.get(0).getRatio() >= 0);
        Assert.assertEquals(symbol, splitsList.get(0).getSymbol());
        Assert.assertTrue(splitsList.get(0).getToFactor() >= 0);
        Assert.assertFalse(splitsList.get(0).getId().isEmpty());
        Assert.assertFalse(splitsList.get(0).getKey().isEmpty());
        Assert.assertFalse(splitsList.get(0).getSubkey().isEmpty());
        Assert.assertTrue(splitsList.get(0).getDate() >= 0);
        Assert.assertTrue(splitsList.get(0).getUpdated() >= 0);
    }

    @SneakyThrows
    @Test(testName = "Check all splits in the response")
    public void checkAllSplitsInTheResponse() {
        String symbol = "AMZN";

        SplitsModel splitsResponse = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel.class);

        List<SplitsModel> splitsList = mapper()
                .readValue(replace(splitsResponse.getSplits()
                        .replace("None", "null")), new TypeReference<List<SplitsModel>>(){});

        for (SplitsModel splitsModel : splitsList) {
            Assert.assertNull(splitsModel.getDeclaredDate());
            Assert.assertFalse(splitsModel.getDescription().isEmpty());
            Assert.assertFalse(splitsModel.getExDate().isEmpty());
            Assert.assertTrue(splitsModel.getFromFactor() >= 0);
            Assert.assertTrue(splitsModel.getRatio() >= 0);
            Assert.assertEquals(symbol, splitsModel.getSymbol());
            Assert.assertTrue(splitsModel.getToFactor() >= 0);
            Assert.assertFalse(splitsModel.getId().isEmpty());
            Assert.assertFalse(splitsModel.getKey().isEmpty());
            Assert.assertFalse(splitsModel.getSubkey().isEmpty());
            Assert.assertTrue(splitsModel.getDate() >= 0);
            Assert.assertTrue(splitsModel.getUpdated() >= 0);
        }
    }

    @Test(testName = "/splits returns correct size of array")
    public void splitsReturnMultipleResponse() {
        String url = splits.replace("splits/", "splits");
        String symbol1 = "AMZN";
        String symbol2 = "NVDA";

        SplitsModel[] splits = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", symbol1)
                .queryParam("symbol", symbol2)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel[].class);

        Assert.assertEquals(splits.length, 2);
    }

    @SneakyThrows
    @Test(testName = "Check response contains list of companies' splits")
    public void checkResponseContainsListOfSplits() {
        String url = splits.replace("splits/", "splits");
        String symbol1 = "AMZN";
        String symbol2 = "NVDA";

        SplitsModel[] model = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", symbol1)
                .queryParam("symbol", symbol2)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel[].class);

        List<SplitsModel> splitsList = new ArrayList<>();

        for (SplitsModel dividendsModel : model) {
            splitsList
                    .addAll(mapper()
                            .readValue(replace(dividendsModel.getSplits()),
                                    new TypeReference<List<SplitsModel>>() {}));
        }

        for (SplitsModel splitsModel : splitsList) {
            Assert.assertNull(splitsModel.getDeclaredDate());
            Assert.assertFalse(splitsModel.getDescription().isEmpty());
            Assert.assertFalse(splitsModel.getExDate().isEmpty());
            Assert.assertTrue(splitsModel.getFromFactor() >= 0);
            Assert.assertTrue(splitsModel.getRatio() >= 0);
            Assert.assertFalse(splitsModel.getSymbol().isEmpty());
            Assert.assertTrue(splitsModel.getToFactor() >= 0);
            Assert.assertFalse(splitsModel.getId().isEmpty());
            Assert.assertFalse(splitsModel.getKey().isEmpty());
            Assert.assertFalse(splitsModel.getSubkey().isEmpty());
            Assert.assertTrue(splitsModel.getDate() >= 0);
            Assert.assertTrue(splitsModel.getUpdated() >= 0);
        }
    }

    @SneakyThrows
    @Test(testName = "/splits returns with 1 year range and equals to IEX", dataProvider = "data-provider")
    public void splitsReturnsWith1YearRangeAndEqualsToIEX(String symbol) {
        SplitsModel splitsResponse = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(splits + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel.class);

        if (splitsResponse.getSplits().isEmpty()) {
            Assert.fail("=============== Splits are empty ===============");
        }
        List<SplitsModel> splitsList = mapper()
                .readValue(replace(splitsResponse.getSplits()
                        .replace("None", "null")), new TypeReference<List<SplitsModel>>(){});

        SplitsModel[] iexResponse = given()
                .spec(REQUEST_SPEC)
                .queryParam("token", "pk_c17b30da5c2c497ba300cf50fcdd8da9")
                .when()
                .get("https://cloud.iexapis.com/stable/stock/" + symbol + "/splits/1y")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(SplitsModel[].class);

        for (int i = 0; i < iexResponse.length; i++) {
            Assert.assertEquals(splitsList.get(i).getDescription(), iexResponse[i].getDescription());
            Assert.assertEquals(splitsList.get(i).getExDate(), iexResponse[i].getExDate());
            Assert.assertEquals(splitsList.get(i).getFromFactor(), iexResponse[i].getFromFactor());
            Assert.assertEquals(splitsList.get(i).getRatio(), iexResponse[i].getRatio());
            Assert.assertEquals(splitsList.get(i).getRefid(), iexResponse[i].getRefid());
            Assert.assertEquals(splitsList.get(i).getSymbol(), iexResponse[i].getSymbol());
            Assert.assertEquals(splitsList.get(i).getToFactor(), iexResponse[i].getToFactor());
            Assert.assertEquals(splitsList.get(i).getId(), iexResponse[i].getId());
            Assert.assertEquals(splitsList.get(i).getKey(), iexResponse[i].getKey());
            Assert.assertEquals(splitsList.get(i).getSubkey(), iexResponse[i].getSubkey());
            Assert.assertEquals(splitsList.get(i).getDate(), iexResponse[i].getDate());
            Assert.assertEquals(splitsList.get(i).getUpdated(), iexResponse[i].getUpdated());
        }
    }
}
