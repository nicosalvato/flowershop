package it.nicosalvato.flowershop.products.repositories;

import it.nicosalvato.flowershop.products.Bundle;
import it.nicosalvato.flowershop.products.Product;

import java.util.Set;

public interface ProductRepository {

    public int count();

    public Product findByCode(String code);

    public Bundle findByProductCodeAndBundleSize(String code, int size);

    public Set<Bundle> findAllByProductCode(String code);

    public void save(Product product);

    public void clear();
}
