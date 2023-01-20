package api.tests;

import api.courier.Courier;
import api.courier.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateCourierTest {

    private Courier courier;
    private CourierClient courierClient;

    private CourierSteps step;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        step = new CourierSteps(courierClient);
    }

    @After
    public void cleanUp() {
        step.deleteCourier(courier);
    }

    @Test
    @DisplayName("Check that courier with all fields is successfully created")
    public void checkCourierCanBeCreatedTrue() {
        courier = step.createCourierWithAllFields();
        ValidatableResponse responseBody = courierClient.create(courier);
        assertEquals(SC_CREATED, step.getStatusCode(responseBody));
        assertEquals(true, step.getSuccessMessage(responseBody));
    }

    @Test
    @DisplayName("Check that identical couriers can't be created")
    public void checkIdenticalCourierNotCreatedTrue() {
        courier = step.createCourierWithAllFields();
        courierClient.create(courier);
        ValidatableResponse responseBody = courierClient.create(courier);
        assertEquals(SC_CONFLICT, step.getStatusCode(responseBody));
    }

    @Test
    @DisplayName("Check that courier with missing fields is not created")
    public void checkCourierWithMissingFieldsNotCreatedTrue() {
        courier = step.createCourierWithMissingField();
        ValidatableResponse responseBody = courierClient.create(courier);
        assertEquals(SC_BAD_REQUEST, step.getStatusCode(responseBody));
        assertEquals("Недостаточно данных для создания учетной записи", step.getInvalidMessage(responseBody));
    }

    @Test
    public void checkICourierWithIdenticalLoginNotCreatedTrue() {
        courier = step.createCourierWithAllFields();
        courierClient.create(courier);
        Courier courier2 = step.createCourierWithSameLogin();
        ValidatableResponse responseBody = courierClient.create(courier2);
        assertEquals(SC_CONFLICT, step.getStatusCode(responseBody));
        assertEquals("Этот логин уже используется. Попробуйте другой.", step.getInvalidMessage(responseBody));
    }
}
