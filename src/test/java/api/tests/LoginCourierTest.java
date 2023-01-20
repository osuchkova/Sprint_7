package api.tests;

import api.courier.Courier;
import api.courier.CourierClient;
import api.courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class LoginCourierTest {

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
    public void checkCanBeAuthorizedTrue() {
        courier = step.createCourierWithAllFields();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_OK, step.getStatusCode(loginResponse));
        courierClient.login(CourierCredentials.from(courier)).assertThat().body("id", notNullValue());
    }

    @Test
    public void checkCantLoginByUnexcitingCourierTrue() {
        courier = step.createUnexcitingCourier();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_NOT_FOUND, step.getStatusCode(loginResponse));
        assertEquals("Учетная запись не найдена", step.getInvalidMessage(loginResponse));
    }

    @Test
    public void checkCantLoginWithMissingFieldTrue() {
        courier = step.createCourierWithMissingField();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_BAD_REQUEST, step.getStatusCode(loginResponse));
        assertEquals("Недостаточно данных для входа", step.getInvalidMessage(loginResponse));
    }

    @Test
    public void checkCantLoginWithInvalidFieldTrue() {
        courier = step.createCourierWithMissingField();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertEquals(SC_BAD_REQUEST, step.getStatusCode(loginResponse));
        assertEquals("Недостаточно данных для входа", step.getInvalidMessage(loginResponse));
    }
}
