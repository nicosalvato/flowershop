package it.nicosalvato.flowershop.products;

import lombok.*;

import java.util.Comparator;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bundle implements Comparable<Bundle> {

    private int bundleSize;
    private double price;

    @Override
    public int compareTo(Bundle o) {
        return Comparator.comparing(Bundle::getBundleSize).compare(o, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bundle bundle = (Bundle) o;
        return getBundleSize() == bundle.getBundleSize() && getPrice() == bundle.getPrice();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBundleSize(), getPrice());
    }
}
