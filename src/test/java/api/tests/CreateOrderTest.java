package api.tests;

import api.order.Order;
import api.order.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> colors;
    private OrderClient orderClient;
    private OrderSteps step;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{{List.of("BLACK")}, {List.of("BLACK", "GRAY")}, {List.of()},};
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        step = new OrderSteps(orderClient);
    }

    @Test
    @DisplayName("Check that order is created")
    public void checkOrderCanBeCreatedTrue() {
        Order order = createDefaultOrder(colors);
        ValidatableResponse responseBody = orderClient.create(order);
        assertEquals(SC_CREATED, step.getStatusCode(responseBody));
        responseBody.assertThat().body("track", notNullValue());
        step.finishOrder(step.getOrderTrack(responseBody));
    }

    public Order createDefaultOrder(List<String> colors) {
        String[] color = colors.toArray(new String[0]);
        return new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "Бульвар Рокоссовского", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
    }
}
