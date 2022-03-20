package it.nicosalvato.flowershop.products;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Bundle implements Comparable<Bundle> {

    private final int bundleSize;
    private final double price;
    private final Product product;

    @Override
    public int compareTo(Bundle o) {
        return Comparator.comparing(Bundle::getBundleSize).compare(o, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bundle bundle = (Bundle) o;
        return getBundleSize() == bundle.getBundleSize() && getProduct().equals(bundle.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBundleSize(), getProduct());
    }
}
