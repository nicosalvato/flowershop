package it.nicosalvato.flowershop.managers;

public class OrderManagerFactory {

    private OrderManagerFactory() {}

    public static OrderManager getInstance(OrderManagerType type) {
        if (type == OrderManagerType.TREE)
            return new OrderManagerTreeImpl();
        else
            return new OrderManagerDPImpl();
    }

    public static OrderManager getInstance(String type) {
        try {
            return getInstance(OrderManagerType.valueOf(type));
        } catch (IllegalArgumentException e) {
            return getInstance(OrderManagerType.DP);
        }
    }
}
