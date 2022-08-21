package com.atqc.framework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.atqc.framework.Config.restApiBaseUri;

public class APIHelper {

    static {
        RestAssured.baseURI = restApiBaseUri;
    }

    public static Response sendPOSTRequest(String path, String model) throws ParseException {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        JSONObject parameters = new JSONObject();
        parameters.put("methodProperties", new JSONParser().parse(model));
        request.body(parameters.toJSONString());

        return request.post(path);
    }
}
