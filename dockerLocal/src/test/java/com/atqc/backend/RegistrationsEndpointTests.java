package com.atqc.backend;

import com.atqc.models.RegistrationsModel;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.atqc.framework.Common.*;
import static com.atqc.framework.Config.userEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

@Log4j2
public class RegistrationsEndpointTests extends BaseTest {

    String registrations = "";

    @DataProvider(name = "data-providerIncorrectValues")
    public Object[][] negativeCases() {
        return new Object[][]{
                {"testtest.com"},
                {"test test@test.com"},
                {"test@test test.com"},
                {"@test.com"},
                {"test@"},
                {"test@test."},
                {".user.name@example.com"},
                {"user.name.@example.com"},
                {"test@test..test.com"},
                {"01234567890123456789012345678901234567890123456789012345" +
                        "6789012345678901234567890123456789012345678901234" +
                        "56789012345678901234567890123456789@test.com"},
                {"test@testcom"},
                {"test@test_test.com"},
                {"test@test.12"},
                {"test@test"}
        };
    }

    @DataProvider(name = "data-providerCorrectValues")
    public Object[][] positiveCases() {
        String userEmailName = userEmail.split("@", 2)[0] + getRandomInt(99999);
        String userEmailHost = userEmail.split("@", 2)[1];
        return new Object[][]{
                {userEmailName + "+TEST@" + userEmailHost},
                {userEmailName + "+123test@" + userEmailHost},
                {userEmailName + "+test-test@" + userEmailHost},
                {userEmailName + "+test_test@" + userEmailHost},
                {userEmailName + "+test.test@" + userEmailHost},
        };
    }

    @Test(testName = "Send request with incorrect email",
            dataProvider = "data-providerIncorrectValues")
    public void sendIncorrectEmail(String email) {
        RegistrationsModel model = new RegistrationsModel();
        model.setEmail(email);

        given()
                .spec(REQUEST_SPEC)
                .body(model)
                .when()
                .post(registrations)
                .then()
                .log().ifError()
                .statusCode(422)
                .body("errors", contains("email must be a well-formed email address"));
    }

    @Test(testName = "Send request with correct email",
            dataProvider = "data-providerCorrectValues")
    public void sendCorrectEmail(String email) {
        RegistrationsModel model = new RegistrationsModel();
        model.setEmail(email);

        given()
                .spec(REQUEST_SPEC)
                .body(model)
                .when()
                .post(registrations)
                .then()
                .log().ifError()
                .statusCode(201);
    }

    @Test(testName = "Send request with email and referral code")
    public void sendEmailAndReferralCode() {
        RegistrationsModel model = new RegistrationsModel();
        model.setEmail(getEmail());
        model.setReferralCode(fakeFixedString(10));

        given()
                .spec(REQUEST_SPEC)
                .body(model)
                .when()
                .post(registrations)
                .then()
                .log().ifError()
                .statusCode(201);
    }

    @Test(testName = "Send request with empty email")
    public void sendEmptyEmail() {
        RegistrationsModel model = new RegistrationsModel();
        model.setEmail("");

        given()
                .spec(REQUEST_SPEC)
                .body(model)
                .when()
                .post(registrations)
                .then()
                .log().ifError()
                .statusCode(422)
                .body("errors", contains("email must not be blank"));
    }

    @Test(testName = "Check email already exists")
    public void emailAlreadyExists() {
        String email = getEmail();
        RegistrationsModel model = new RegistrationsModel();
        model.setEmail(email);

        given()
                .spec(REQUEST_SPEC)
                .body(model)
                .when()
                .post(registrations)
                .then()
                .statusCode(201);

        given()
                .spec(REQUEST_SPEC)
                .body(model)
                .when()
                .post(registrations)
                .then()
                .log().ifError()
                .statusCode(409)
                .body("message", equalTo("Email " + email + " is registered"));
    }

}
