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

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(orderSize + " " + code + " $" + new DecimalFormat("0.00").format(this.getTotal()));
        for (BundleDelivery item: bundleDeliveryList) {
            sb.append("\n").append(item.prettyPrint());
        }
        return sb.toString();
    }

    public String prettyPrint(String msg) {
        return orderSize + " " + code + " $" + new DecimalFormat("0.00").format(this.getTotal()) + "\n - " + msg;
    }
}
