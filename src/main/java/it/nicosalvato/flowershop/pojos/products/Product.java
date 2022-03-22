package it.nicosalvato.flowershop.pojos.products;

import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Comparable<Product> {

    private String name;
    private String code;
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
