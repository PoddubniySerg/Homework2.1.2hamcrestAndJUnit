import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderTest {

    private static List<Order> orderList;

    @BeforeAll
    public static void initOrdersCollection() {
        orderList = List.of(
                new Order("ЛС000002563", "Караблина Элла", ""),
                new Order("ЛС000005249", "Наймушина Наталия", "12.03.2022"),
                new Order("ЛС000013756", "Пик Ирина", "23.07.2022"),
                new Order("ЛС000013788", "Гиберт Ирина", new SimpleDateFormat("dd.MM.yyyy").format(new Date()))
        );
        System.out.println("Start tests");
    }

    @AfterAll
    public static void completeTests() {
        System.out.println("Tests complete");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Start test");
    }

    @AfterEach
    public void completeTest() {
        System.out.println("Test complete");
    }

    @Test
    public void test_getOrderNumber() {
        //arrange
        Order order = orderList.get(0);
        String expected = "ЛС000002563";

        //act
        String orderNumber = order.getOrderNumber();

        //assert
        assertThat(orderNumber, equalTo(expected));
    }

    @Test
    public void test_getManager() {
        //arrange
        Order order = orderList.get(0);
        String expected = "Караблина Элла";

        //act
        String manager = order.getManager();

        //assert
        assertThat(manager, equalTo(expected));
    }

    @ParameterizedTest
    @MethodSource("parametersFor_test_getDateStr")
    public void test_getDateStr(Order order, String expected) {
        //act
        String dateStr = order.getDateStr();
        //assert
        assertThat(dateStr, equalTo(expected));
    }

    public static Stream<Arguments> parametersFor_test_getDateStr() {
        return Stream.of(
                Arguments.of(orderList.get(0), ""),
                Arguments.of(orderList.get(1), "12.03.2022"),
                Arguments.of(orderList.get(2), "23.07.2022"),
                Arguments.of(orderList.get(3), new SimpleDateFormat("dd.MM.yyyy").format(new Date()))
        );
    }

    @ParameterizedTest
    @MethodSource("parametersFor_test_getDate")
    public void test_getDateStr(Order order, Date expected) {
        if (order == null) {
            System.out.println("Error parsing date");
            return;
        }
        //act
        Date date = order.getDate();
        //assert
        assertThat(date, equalTo(expected));
    }

    public static Stream<Arguments> parametersFor_test_getDate() throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");
        String today = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        return Stream.of(
                Arguments.of(orderList.get(0), null),
                Arguments.of(orderList.get(1), parser.parse("12.03.2022")),
                Arguments.of(orderList.get(2), parser.parse("23.07.2022")),
                Arguments.of(orderList.get(3), parser.parse(today))
        );
    }

    @ParameterizedTest
    @MethodSource("parametersFor_tests_setDate_and_getPriority")
    public void tests_setDate_and_getPriority(Order order, Date date, int expected) {
        //act
        order.setDate(date);
        int priority = order.getPriority();
        //assert
        assertThat(priority, equalTo(expected));
    }

    public static Stream<Arguments> parametersFor_tests_setDate_and_getPriority() throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");
        String today = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        return Stream.of(
                Arguments.of(orderList.get(0), null, 0),
                Arguments.of(orderList.get(1), parser.parse("12.03.2022"), 1),
                Arguments.of(orderList.get(2), parser.parse("23.07.2022"), 0),
                Arguments.of(orderList.get(3), parser.parse(today), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("parametersFor_test_equals")
    public void test_equals(Order order, Order expected) {
        //assert
        assertThat(order, equalTo(expected));
    }

    public static Stream<Arguments> parametersFor_test_equals() {
        return Stream.of(
                Arguments.of(orderList.get(0),
                        new Order(orderList.get(0).getOrderNumber(), orderList.get(0).getManager(), "12.03.2022")),
                Arguments.of(orderList.get(1),
                        new Order(orderList.get(1).getOrderNumber(), orderList.get(1).getManager(), "12.03.2022")),
                Arguments.of(orderList.get(2),
                        new Order(orderList.get(2).getOrderNumber(), orderList.get(2).getManager(), "12.03.2022")),
                Arguments.of(orderList.get(3),
                        new Order(orderList.get(3).getOrderNumber(), orderList.get(3).getManager(), ""))
        );
    }

    @ParameterizedTest
    @MethodSource("parametersFor_test_hashCode")
    public void test_hashCode(Order order, int expected) {
        //act
        int hashCode = order.hashCode();
        //assert
        assertThat(hashCode, equalTo(expected));
    }

    public static Stream<Arguments> parametersFor_test_hashCode() {
        return Stream.of(
                Arguments.of(orderList.get(0),
                        new Order(orderList.get(0).getOrderNumber(), orderList.get(0).getManager(), "12.03.2022").hashCode()),
                Arguments.of(orderList.get(1),
                        new Order(orderList.get(1).getOrderNumber(), orderList.get(1).getManager(), "12.03.2022").hashCode()),
                Arguments.of(orderList.get(2),
                        new Order(orderList.get(2).getOrderNumber(), orderList.get(2).getManager(), "12.03.2022").hashCode()),
                Arguments.of(orderList.get(3),
                        new Order(orderList.get(3).getOrderNumber(), orderList.get(3).getManager(), "").hashCode())
        );
    }

    @Test
    public void test_toString() {
        //arrange
        String today = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        String[] expected = {
                "ЛС000002563 Караблина Элла ",
                "ЛС000005249 Наймушина Наталия 12.03.2022",
                "ЛС000013756 Пик Ирина 23.07.2022",
                "ЛС000013788 Гиберт Ирина " + today
        };
        //act
        String[] ordersToString = orderList.stream().map(Order::toString).toArray(String[]::new);
        //assert
        assertThat(ordersToString, arrayContaining(expected));

    }

    @ParameterizedTest
    @MethodSource("parametersFor_test_compareTo")
    public void test_compareTo(Order order1, Order order2) {
        //assert
        assertThat(order1, greaterThan(order2));
    }

    public static Stream<Arguments> parametersFor_test_compareTo() {
        return Stream.of(
                Arguments.of(orderList.get(0),
                        new Order(orderList.get(0).getOrderNumber(), orderList.get(3).getManager(), "12.03.2022"), true),
                Arguments.of(orderList.get(1),
                        new Order(orderList.get(1).getOrderNumber(), orderList.get(1).getManager(), "23.07.2022"), true),
                Arguments.of(orderList.get(2),
                        new Order(orderList.get(2).getOrderNumber(), orderList.get(2).getManager(), "10.07.2022"), true),
                Arguments.of(orderList.get(3),
                        new Order(orderList.get(0).getOrderNumber(), orderList.get(3).getManager(), ""), true)
        );
    }
}