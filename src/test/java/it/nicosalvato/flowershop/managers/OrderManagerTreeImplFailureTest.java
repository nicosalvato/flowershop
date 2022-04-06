package it.nicosalvato.flowershop.managers;

import it.nicosalvato.flowershop.services.ConfigurationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class OrderManagerTreeImplFailureTest {

    OrderManager orderManager = new OrderManagerTreeImpl();
    ConfigurationService configurationService = ConfigurationService.getInstance();

    @Test
    @DisplayName("Test the plain simple fact that this algorithm is flawed")
    void testFailingOrder() {
        String configuration = """
                [
                  {
                    "name": "Roses",
                    "code": "R12",
                    "bundles": [
                      {
                        "bundleSize": 60,
                        "price": 60.00
                      },
                      {
                        "bundleSize": 40,
                        "price": 40.00
                      },
                      {
                        "bundleSize": 2,
                        "price": 2.00
                      }
                    ]
                  }
                ]
                """;
        configurationService.loadFromJson(configuration);

        String order = "80 R12";
        String expectedDelivery = """
                80 R12 $80.00
                 - 2 x 40 $40.00""";
        Assertions.assertNotEquals(expectedDelivery,
                orderManager.processOrder(new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8))));

        String actualDelivery = """
                80 R12 $80.00
                 - 1 x 60 $60.00
                 - 10 x 2 $2.00""";
        Assertions.assertEquals(actualDelivery,
                orderManager.processOrder(new ByteArrayInputStream(order.getBytes(StandardCharsets.UTF_8))));


    }
}
