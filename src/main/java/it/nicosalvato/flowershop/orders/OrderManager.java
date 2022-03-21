package it.nicosalvato.flowershop.orders;

import java.io.InputStream;

public interface OrderManager {
    public String processOrder(InputStream order);
}
