package com.atqc.backend;

import com.atqc.framework.Common;
import com.atqc.models.PeersModel;
import lombok.SneakyThrows;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class PeersGroupEndpointTests extends BaseTest {

    String peers = "";

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
                {"NI-B"}
        };
    }

    @Test(testName = "/peers with wrong data returns 404, Not found")
    public void getPeersReturnsUnknownSymbol() {
        String symbol = Common.fakeFixedString(10);

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(peers + symbol)
                .then()
                .log().ifError()
                .statusCode(404)
                .body("detail", containsString("Could not find peer groups for " + symbol));
    }

    @Test(testName = "Check peers without symbol returns 422 code")
    public void checkSplitsWithoutSymbolReturns422() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(peers)
                .then()
                .statusCode(422)
                .body("detail", containsString("Please provide at least one symbol"));
    }

    @Test(testName = "/peers returns code 200")
    public void peersReturns200Code() {

        given()
                .spec(REQUEST_SPEC)
                .when()
                .get(peers + "AAPL")
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    @Test(testName = "/peers returns correct model", dataProvider = "data-provider")
    public void peersReturnsCorrectModel(String symbol) {

        PeersModel peersResponse = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(peers + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(PeersModel.class);

        Assert.assertNotNull(peersResponse.getSymbol());
        Assert.assertNotNull(peersResponse.getPeers());
    }

    @SneakyThrows
    @Test(testName = "Check all peers in the response")
    public void checkAllSplitsInTheResponse() {
        String symbol = "AMZN";

        PeersModel peersResponse = given()
                .spec(REQUEST_SPEC)
                .when()
                .get(peers + symbol)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(PeersModel.class);

        List<String> peers = Arrays.asList(peersResponse.getPeers()
                .replace(" ", "").split(","));

        Assert.assertTrue(peers.size() > 0);
    }

    @Test(testName = "/peers returns correct size of array")
    public void peersReturnMultipleResponse() {
        String url = peers.replace("peers/", "peers");
        String symbol1 = "AMZN";
        String symbol2 = "NVDA";

        PeersModel[] peers = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", symbol1)
                .queryParam("symbol", symbol2)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(PeersModel[].class);

        Assert.assertEquals(peers.length, 2);
    }

    @SneakyThrows
    @Test(testName = "Check response contains list of peers")
    public void checkResponseContainsListOfSplits() {
        String url = peers.replace("peers/", "peers");
        String symbol1 = "AMZN";
        String symbol2 = "NVDA";

        PeersModel[] response = given()
                .spec(REQUEST_SPEC)
                .queryParam("symbol", symbol1)
                .queryParam("symbol", symbol2)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(PeersModel[].class);

        for (PeersModel peersModel : response) {
            Assert.assertNotNull(peersModel.getSymbol());
            Assert.assertFalse(peersModel.getPeers().isEmpty());
        }
    }
}
