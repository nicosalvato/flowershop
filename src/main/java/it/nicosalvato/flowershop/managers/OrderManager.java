package it.nicosalvato.flowershop.managers;

import java.io.InputStream;

public interface OrderManager {
    public String processOrder(InputStream order);
}
