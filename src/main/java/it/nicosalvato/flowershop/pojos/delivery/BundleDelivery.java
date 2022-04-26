package it.nicosalvato.flowershop.pojos.delivery;

import it.nicosalvato.flowershop.pojos.products.Bundle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BundleDelivery {
    private int amount;
    private Bundle bundle;
}
