package it.nicosalvato.flowershop.utils;

import it.nicosalvato.flowershop.pojos.delivery.BundleDelivery;
import it.nicosalvato.flowershop.pojos.delivery.ProductDelivery;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDeliveryPrinter {

    private ProductDeliveryPrinter() {}

    public static String printAsText(List<ProductDelivery> productDeliveryList) {
        return productDeliveryList.stream()
                .map(ProductDeliveryPrinter::printAsText)
                .collect(Collectors.joining("\n"));
    }

    public static String printAsText(ProductDelivery productDelivery) {
        StringBuilder sb = new StringBuilder(productDelivery.getOrderSize() + " " +
                        productDelivery.getCode() + " $" +
                        new DecimalFormat("0.00").format(productDelivery.getTotal()));

        if (!productDelivery.getBundleDeliveryList().isEmpty()) {
            for (BundleDelivery item : productDelivery.getBundleDeliveryList()) {
                sb.append("\n").append(printBundleAsText(item));
            }
        } else {
            sb.append("\n").append(" - Unable to deliver order");
        }

        return sb.toString();
    }

    public static String printBundleAsText(BundleDelivery bundleDelivery) {
        return " - " + bundleDelivery.getAmount() + " x " +
                bundleDelivery.getBundle().getBundleSize() + " $" +
                new DecimalFormat("0.00").format(bundleDelivery.getBundle().getPrice());
    }
}
