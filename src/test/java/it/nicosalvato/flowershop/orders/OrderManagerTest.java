package it.nicosalvato.flowershop.orders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class OrderManagerTest {

    OrderManager orderManager;

    @BeforeEach
    void setUp() {
        orderManager = new OrderManager();
    }

    @Test
    @DisplayName("Test order 1 (10 R12)")
    void testOrder1Processing() throws FileNotFoundException {
        FileInputStream order = new FileInputStream("src/test/resources/order_1.txt");
        String expectedDelivery = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/delivery_1.txt"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Assertions.assertEquals(expectedDelivery, orderManager.processOrder(order));
    }

    @Test
    @DisplayName("Test order 2 (15 L09)")
    void testOrder2Processing() throws FileNotFoundException {
        FileInputStream order = new FileInputStream("src/test/resources/order_2.txt");
        String expectedDelivery = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/delivery_2.txt"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Assertions.assertEquals(expectedDelivery, orderManager.processOrder(order));
    }
}
