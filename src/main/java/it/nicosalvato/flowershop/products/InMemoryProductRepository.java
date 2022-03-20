package it.nicosalvato.flowershop.products;

import java.util.Set;
import java.util.TreeSet;

public class InMemoryProductRepository implements ProductRepository {

    private static Set<Product> products;
    static {
        products = new TreeSet<>();
        Product roses = new Product("Roses", "R12");
        Set<Bundle> rosesBundles = Set.of(
                new Bundle(5, 6.99, roses),
                new Bundle(10, 12.99, roses)
        );
        roses.setBundles(rosesBundles);
        products.add(roses);

        Product lilies = new Product("Lilies", "L09");
        Set<Bundle> liliesBundles = Set.of(
                new Bundle(3, 9.95, lilies),
                new Bundle(6, 16.95, lilies),
                new Bundle(9, 24.95, lilies)
        );
        lilies.setBundles(liliesBundles);
        products.add(lilies);

        Product tulips = new Product("Tulips", "T58");
        Set<Bundle> tulipsBundles = Set.of(
                new Bundle(3, 5.95, tulips),
                new Bundle(5, 9.95, tulips),
                new Bundle(9, 16.99, tulips)
        );
        tulips.setBundles(tulipsBundles);
        products.add(tulips);
    }

    @Override
    public Product findByCode(String code) {
        return products.stream().filter(p -> p.getCode().equals(code)).findFirst().orElseThrow();
    }

    @Override
    public Bundle findByProductCodeAndBundleSize(String code, int size) {
        Product product = products.stream().filter(p -> p.getCode().equals(code)).findFirst().orElseThrow();
        return product.getBundles().stream().filter(b -> b.getBundleSize() == size).findFirst().orElseThrow();
    }

    @Override
    public Set<Bundle> findAllByProductCode(String code) {
        return products.stream()
                .filter(p -> p.getCode().equals(code))
                .findFirst()
                .map(Product::getBundles)
                .orElseThrow();
    }
}
