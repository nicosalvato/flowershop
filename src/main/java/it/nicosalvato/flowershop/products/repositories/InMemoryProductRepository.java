package it.nicosalvato.flowershop.products.repositories;

import it.nicosalvato.flowershop.products.Bundle;
import it.nicosalvato.flowershop.products.Product;

import java.util.Set;
import java.util.TreeSet;

public class InMemoryProductRepository implements ProductRepository {

    private InMemoryProductRepository() {}

    private static class SingletonHolder {
        public static final ProductRepository instance = new InMemoryProductRepository();
    }

    public static ProductRepository getInstance() {
        return InMemoryProductRepository.SingletonHolder.instance;
    }

    private static final Set<Product> products = new TreeSet<>();

    @Override
    public int count() {
        return products.size();
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

    @Override
    public void save(Product product) {
        products.add(product);
    }

    @Override
    public void clear() {
        products.clear();
    }
}
