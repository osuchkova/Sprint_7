package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateCourierTest {

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
    @DisplayName("Check that courier with all fields is successfully created")
    public void checkCourierCanBeCreatedTrue(){
        courier = createCourierWithAllFields();
        ValidatableResponse responseBody = courierClient.create(courier);
        assertEquals(SC_CREATED, getStatusCode(responseBody));
        assertEquals(true, getSuccessMessage(responseBody));
    }

    @Test
    @DisplayName("Check that identical couriers can't be created")
    public void checkIdenticalCourierNotCreatedTrue(){
        courier = createCourierWithAllFields();
        courierClient.create(courier);
        ValidatableResponse responseBody = courierClient.create(courier);
        assertEquals(SC_CONFLICT, getStatusCode(responseBody));
    }

    @Test
    @DisplayName("Check that courier with missing fields is not created")
    public void checkCourierWithMissingFieldsNotCreatedTrue(){
        courier = createCourierWithMissingField();
        ValidatableResponse responseBody = courierClient.create(courier);
        assertEquals(SC_BAD_REQUEST, getStatusCode(responseBody));
        assertEquals("Недостаточно данных для создания учетной записи", getInvalidMessage(responseBody));
    }

    @Test
    public void checkICourierWithIdenticalLoginNotCreatedTrue(){
        courier = createCourierWithAllFields();
        courierClient.create(courier);
        Courier courier2 = createCourierWithSameLogin();
        ValidatableResponse responseBody = courierClient.create(courier2);
        assertEquals(SC_CONFLICT, getStatusCode(responseBody));
        assertEquals("Этот логин уже используется. Попробуйте другой.", getInvalidMessage(responseBody));
    }

    @Step("Create courier with all fields")
    public Courier createCourierWithAllFields(){
        return new Courier("abctw", "test", "Olga");
    }

    @Step("Create courier with missing field")
    public Courier createCourierWithMissingField(){
        return new Courier("sova", "", "Olga");
    }

    @Step("Create courier with repeating login")
    public Courier createCourierWithSameLogin(){
        return new Courier("abc", "test1", "Olga1");
    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse responseBody){
        return responseBody.extract().response().statusCode();
    }

    @Step("Get successfully created response message")
    public Boolean getSuccessMessage(ValidatableResponse responseBody){
        return responseBody.extract().response().path("ok");
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
