package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class LoginCourierTest {

    private Courier courier;
    private CourierClient courierClient;

    @Before
    public void setUp(){
        courierClient = new CourierClient();
    }

    @After
    public void cleanUp(){
        deleteCourier(courier);
    }

    @Test
    public void checkCanBeAuthorizedTrue(){
        courier = createCourierWithAllFields();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_OK, getStatusCode(loginResponse));
        courierClient.login(CourierCredentials.from(courier)).assertThat().body("id", notNullValue());
    }

    @Test
    public void checkCantLoginByUnexcitingCourierTrue(){
        courier = createUnexcitingCourier();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_NOT_FOUND, getStatusCode(loginResponse));
        assertEquals("Учетная запись не найдена", getInvalidMessage(loginResponse));
    }

    @Test
    public void checkCantLoginWithMissingFieldTrue(){
        courier = createCourierWithMissingField();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_BAD_REQUEST, getStatusCode(loginResponse));
        assertEquals("Недостаточно данных для входа", getInvalidMessage(loginResponse));
    }

    @Test
    public void checkCantLoginWithInvalidFieldTrue(){
        courier = createCourierWithMissingField();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_BAD_REQUEST, getStatusCode(loginResponse));
        assertEquals("Недостаточно данных для входа", getInvalidMessage(loginResponse));
    }

    @Step("Create courier with all credentials")
    public Courier createCourierWithAllFields(){
        return new Courier("sffgzwg", "test", "Olga");
    }

    @Step("Create courier with missing field")
    public Courier createCourierWithMissingField(){
        return new Courier("sova", "", "Olga");
    }

    @Step("Create courier with invalid field")
    public Courier createCourierWithInvalidField(){
        return new Courier("sffgzwg", "test1", "Olga");
    }
    @Step("Create unexciting courier")
    public Courier createUnexcitingCourier(){
        return new Courier("ysehshse", "352232523", "Olga");
    }
    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse responseBody){
        return responseBody.extract().response().statusCode();
    }

    @Step("Get invalid response message")
    public String getInvalidMessage(ValidatableResponse responseBody){
        return responseBody.extract().response().path("message");
    }
    @Step("Get courier id")
    public int getCourierId(Courier courier){
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        if (getStatusCode(loginResponse) == 200) {
            return loginResponse.extract().path("id");}
        else return 0;
    }

    @Step("Delete courier")
    public void deleteCourier(Courier courier){
        if (getCourierId(courier)!=0) courierClient.delete(getCourierId(courier));
    }
}
