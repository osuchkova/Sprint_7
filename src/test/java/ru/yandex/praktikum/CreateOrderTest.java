package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> colors;
    private OrderClient orderClient;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("BLACK", "GRAY")},
                {List.of()},
        };
    }

    @Before
    public void setUp(){
       orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Check that order is created")
    public void checkOrderCanBeCreatedTrue(){
        Order order = createDefaultOrder(colors);
        ValidatableResponse responseBody = orderClient.create(order);
        assertEquals(SC_CREATED, getStatusCode(responseBody));
        responseBody.assertThat().body("track", notNullValue());
        finishOrder(getOrderTrack(responseBody));
    }

    public Order createDefaultOrder(List<String> colors) {
        String[] color = colors.toArray(new String[0]);
        return new Order(
                "Naruto", "Uchiha", "Konoha, 142 apt.", "Бульвар Рокоссовского",
                "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse responseBody){
        return responseBody.extract().response().statusCode();
    }

    @Step("Get order track")
    public Integer getOrderTrack(ValidatableResponse response){
        if (getStatusCode(response) == 201) {
            return response.extract().path("track");}
        else return 0;
    }

    @Step("Cancel order")
    public void finishOrder(Integer track){
        System.out.println(track);
        if (track!=0) orderClient.finish(track);
    }

}
