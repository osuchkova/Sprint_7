package ru.yandex.praktikum;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierClient extends Client{

    private static final String PATH = "api/v1/courier";
    private static final String PATH_LOGIN = "api/v1/courier/login";

    public ValidatableResponse create(Courier courier){
        return given()
              .spec(getSpec())
              .body(courier)
              .when()
              .post(PATH)
              .then().log().all();
    }

    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(PATH_LOGIN)
                .then().log().all();
    }

    public ValidatableResponse delete(int id){
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .delete(PATH + "/" + Integer.toString(id))
                .then().log().all();
    }
}
