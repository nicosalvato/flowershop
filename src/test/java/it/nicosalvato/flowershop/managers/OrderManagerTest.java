package it.nicosalvato.flowershop.managers;

import it.nicosalvato.flowershop.services.ConfigurationService;
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
    ConfigurationService configurationService = ConfigurationService.getInstance();

    @BeforeEach
    void setUp() {
        orderManager = new EfficientOrderManager();
        configurationService.loadFromFile("src/test/resources/configuration_3.json");
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

    @Test
    @DisplayName("Test order 3 (13 T58)")
    void testOrder3Processing() throws FileNotFoundException {
        FileInputStream order = new FileInputStream("src/test/resources/order_3.txt");
        String expectedDelivery = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/delivery_3.txt"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Assertions.assertEquals(expectedDelivery, orderManager.processOrder(order));
    }

    @Test
    @DisplayName("Test order 4 (10 R12, 15 L09, 13 T58)")
    void testOrder4Processing() throws FileNotFoundException {
        FileInputStream order = new FileInputStream("src/test/resources/order_4.txt");
        String expectedDelivery = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/delivery_4.txt"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Assertions.assertEquals(expectedDelivery, orderManager.processOrder(order));
    }

    @Test
    @DisplayName("Test unsupported order (9 R12)")
    void testUnsupportedOrderProcessing() throws FileNotFoundException {
        FileInputStream order = new FileInputStream("src/test/resources/order_5.txt");
        String expectedDelivery = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/delivery_5.txt"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Assertions.assertEquals(expectedDelivery, orderManager.processOrder(order));
    }

    @Test
    @DisplayName("Test tricky subtree order (15 T58)")
    void testOrder6Processing() throws FileNotFoundException {
        FileInputStream order = new FileInputStream("src/test/resources/order_6.txt");
        String expectedDelivery = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/delivery_6.txt"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Assertions.assertEquals(expectedDelivery, orderManager.processOrder(order));
    }
}
