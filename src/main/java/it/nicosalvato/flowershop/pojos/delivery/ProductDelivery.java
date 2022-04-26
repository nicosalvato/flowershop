package it.nicosalvato.flowershop.pojos.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DecimalFormat;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProductDelivery {
    private String code;
    private int orderSize;
    private List<BundleDelivery> bundleDeliveryList;

    public double getTotal() {
        return bundleDeliveryList.stream()
                .map(item -> item.getAmount() * item.getBundle().getPrice())
                .reduce(0.0, Double::sum);
    }
}
