package it.nicosalvato.flowershop.managers;

import it.nicosalvato.flowershop.services.ConfigurationService;
import it.nicosalvato.flowershop.utils.ProductDeliveryPrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class OrderManagerImplTest {

    OrderManager orderManager;
    ConfigurationService configurationService = ConfigurationService.getInstance();

    @BeforeEach
    void setUp() {
        orderManager = new OrderManagerImpl();
        configurationService.loadFromFile("src/test/resources/configuration.json");
    }

    @Test
    @DisplayName("Test order (10 R12)")
    void testRosesOrderProcessing() {
        String order = "10 R12";
        String expectedDelivery = """
                10 R12 $12.99
                 - 1 x 10 $12.99""";
        InputStream orderAsStream = new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedDelivery, ProductDeliveryPrinter.printAsText(orderManager.processOrder(orderAsStream)));
    }

    @Test
    @DisplayName("Test order (15 L09)")
    void testLiliesOrderProcessing() {
        String order = "15 L09";
        String expectedDelivery = """
                15 L09 $41.90
                 - 1 x 9 $24.95
                 - 1 x 6 $16.95""";
        InputStream orderAsStream = new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedDelivery, ProductDeliveryPrinter.printAsText(orderManager.processOrder(orderAsStream)));
    }

    @Test
    @DisplayName("Test order (13 T58)")
    void testTulipsOrderProcessing() {
        String order = "13 T58";
        String expectedDelivery = """
                13 T58 $25.85
                 - 2 x 5 $9.95
                 - 1 x 3 $5.95""";
        InputStream orderAsStream = new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedDelivery, ProductDeliveryPrinter.printAsText(orderManager.processOrder(orderAsStream)));
    }

    @Test
    @DisplayName("Test order (10 R12, 15 L09, 13 T58)")
    void testCompoundOrderProcessing() {
        String order = """
                10 R12
                15 L09
                13 T58""";
        String expectedDelivery = """
                10 R12 $12.99
                 - 1 x 10 $12.99
                15 L09 $41.90
                 - 1 x 9 $24.95
                 - 1 x 6 $16.95
                13 T58 $25.85
                 - 2 x 5 $9.95
                 - 1 x 3 $5.95""";
        InputStream orderAsStream = new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedDelivery, ProductDeliveryPrinter.printAsText(orderManager.processOrder(orderAsStream)));
    }

    @Test
    @DisplayName("Test unsupported order (9 R12)")
    void testUnsupportedOrderProcessing() {
        String order = "9 R12";
        String expectedDelivery = """
                9 R12 $0.00
                 - Unable to deliver order""";
        InputStream orderAsStream = new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedDelivery, ProductDeliveryPrinter.printAsText(orderManager.processOrder(orderAsStream)));
    }

    @Test
    @DisplayName("Test big flower order (1208 L09)")
    void testBigUnsupportedOrderProcessing() {
        String order = "1208 L09";
        String expectedDelivery = """
                1208 L09 $0.00
                 - Unable to deliver order""";
        InputStream orderAsStream = new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedDelivery, ProductDeliveryPrinter.printAsText(orderManager.processOrder(orderAsStream)));
    }
}
