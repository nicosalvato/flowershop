package it.nicosalvato.flowershop.products.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.nicosalvato.flowershop.products.Product;
import it.nicosalvato.flowershop.products.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.products.repositories.ProductRepository;

import java.util.List;

public class ProductService {

    ProductRepository productRepository;

    private ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static class SingletonHolder {
        public static final ProductService instance = new ProductService(InMemoryProductRepository.getInstance());
    }

    public static ProductService getInstance() {
        return ProductService.SingletonHolder.instance;
    }

    public void readProductsFromJson(String json) {
        try {
            List<Product> products = new ObjectMapper().readValue(json, new TypeReference<List<Product>>() {});
            products.forEach(product -> productRepository.save(product));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
