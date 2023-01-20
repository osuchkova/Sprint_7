package steps;

import api.order.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class OrderSteps {

    private OrderClient orderClient;

    public OrderSteps(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse responseBody) {
        return responseBody.extract().response().statusCode();
    }

    @Step("Get order track")
    public Integer getOrderTrack(ValidatableResponse response) {
        if (getStatusCode(response) == 201) {
            return response.extract().path("track");
        } else return 0;
    }

    @Step("Cancel order")
    public void finishOrder(Integer track) {
        System.out.println(track);
        if (track != 0) orderClient.finish(track);
    }
}
