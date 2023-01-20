package steps;

import api.courier.Courier;
import api.courier.CourierClient;
import api.courier.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class CourierSteps {

    private CourierClient courierClient;

    public CourierSteps(CourierClient courierClient) {
        this.courierClient = courierClient;
    }

    @Step("Create courier with all fields")
    public Courier createCourierWithAllFields() {
        return new Courier("abctw", "test", "Olga");
    }

    @Step("Create courier with missing field")
    public Courier createCourierWithMissingField() {
        return new Courier("sova", "", "Olga");
    }

    @Step("Create courier with repeating login")
    public Courier createCourierWithSameLogin() {
        return new Courier("abc", "test1", "Olga1");
    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse responseBody) {
        return responseBody.extract().response().statusCode();
    }

    @Step("Get successfully created response message")
    public Boolean getSuccessMessage(ValidatableResponse responseBody) {
        return responseBody.extract().response().path("ok");
    }

    @Step("Get invalid response message")
    public String getInvalidMessage(ValidatableResponse responseBody) {
        return responseBody.extract().response().path("message");
    }

    @Step("Get courier id")
    public int getCourierId(Courier courier) {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        if (getStatusCode(loginResponse) == 200) {
            return loginResponse.extract().path("id");
        } else return 0;
    }

    @Step("Delete courier")
    public void deleteCourier(Courier courier) {
        if (getCourierId(courier) != 0) courierClient.delete(getCourierId(courier));
    }

    @Step("Create courier with invalid field")
    public Courier createCourierWithInvalidField() {
        return new Courier("sffgzwg", "test1", "Olga");
    }

    @Step("Create unexciting courier")
    public Courier createUnexcitingCourier() {
        return new Courier("ysehshse", "352232523", "Olga");
    }
}
