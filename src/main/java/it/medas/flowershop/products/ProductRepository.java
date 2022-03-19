package it.medas.flowershop.products;

import java.util.Set;

public interface ProductRepository {

    public Product findByCode(String code);

    public Bundle findByProductCodeAndBundleSize(String code, int size);

    public Set<Bundle> findAllByProductCode(String code);
}
