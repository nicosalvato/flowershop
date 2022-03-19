package it.medas.flowershop.products;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor()
public class Product implements Comparable<Product> {

    private final String name;
    private final String code;
    private Set<Bundle> bundles;

    @Override
    public int compareTo(Product o) {
        return o.code.compareTo(this.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getCode().equals(product.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
