package it.nicosalvato.flowershop.managers;

import java.io.InputStream;

public interface OrderManager {

    String DEFAULT_UNDELIVERABLE_PRODUCT_EX_MESSAGE = "Unable to deliver order";

    String processOrder(InputStream order);
}
