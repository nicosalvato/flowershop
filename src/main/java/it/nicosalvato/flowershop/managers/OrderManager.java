package it.nicosalvato.flowershop.managers;

import it.nicosalvato.flowershop.pojos.delivery.ProductDelivery;

import java.io.InputStream;
import java.util.List;

public interface OrderManager {

    String DEFAULT_UNDELIVERABLE_PRODUCT_EX_MESSAGE = "Unable to deliver order";

    List<ProductDelivery> processOrder(InputStream order);
}
