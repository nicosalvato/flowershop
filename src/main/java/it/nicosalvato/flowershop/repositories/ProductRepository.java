package it.nicosalvato.flowershop.repositories;

import it.nicosalvato.flowershop.pojos.products.Bundle;
import it.nicosalvato.flowershop.pojos.products.Product;

import java.util.Set;

public interface ProductRepository {

    public int count();

    public Product findByCode(String code);

    public Bundle findByProductCodeAndBundleSize(String code, int size);

    public Set<Bundle> findAllByProductCode(String code);

    public void save(Product product);

    public void clear();
}
