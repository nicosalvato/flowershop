package it.nicosalvato.flowershop.repositories;

import it.nicosalvato.flowershop.pojos.products.Bundle;
import it.nicosalvato.flowershop.pojos.products.Product;

import java.util.Set;

public interface ProductRepository {

    public int count();

    public Product findByCode(String code);

    public Set<Bundle> findAllBundlesByProductCode(String code);

    public void save(Product product);

    public void clear();
}
