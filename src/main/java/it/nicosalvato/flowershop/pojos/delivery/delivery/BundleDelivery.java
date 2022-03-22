package it.nicosalvato.flowershop.pojos.delivery.delivery;

import it.nicosalvato.flowershop.pojos.products.Bundle;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DecimalFormat;

@Getter
@AllArgsConstructor
public class BundleDelivery {
    private int amount;
    private Bundle bundle;

    public String prettyPrint() {
        return " - " + amount + " x " + bundle.getBundleSize() + " $" + new DecimalFormat("0.00").format(bundle.getPrice());
    }
}
