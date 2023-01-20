package api.order;

import api.Client;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH = "api/v1/orders";
    private static final String PATH_FINISH = "api/v1/orders/finish";

    public ValidatableResponse create(Order order) {
        return given().spec(getSpec()).body(order).when().post(PATH).then().log().all();
    }

    public ValidatableResponse finish(Integer track) {
        return given().spec(getSpec()).body("").when().put(PATH_FINISH + "/" + track).then().log().all();
    }
}
