package it.nicosalvato.flowershop.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.nicosalvato.flowershop.exceptions.ConfigurationException;
import it.nicosalvato.flowershop.pojos.products.Product;
import it.nicosalvato.flowershop.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.repositories.ProductRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Manages Flower Shop products configuration.
 */
public class ConfigurationService {

    ProductRepository productRepository;

    private ConfigurationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static class SingletonHolder {
        public static final ConfigurationService instance = new ConfigurationService(InMemoryProductRepository.getInstance());
    }

    public static ConfigurationService getInstance() {
        return ConfigurationService.SingletonHolder.instance;
    }

    public void loadFromJson(String json) throws ConfigurationException {
        try {
            List<Product> products = new ObjectMapper().readValue(json, new TypeReference<List<Product>>() {});
            products.forEach(product -> productRepository.save(product));
        } catch (JsonProcessingException e) {
            throw new ConfigurationException("Flower Shop configuration failed: " + e.getMessage());
        }
    }

    public void loadFromFile(String path) throws ConfigurationException {
        try {
            String json = Files.readString(Path.of(path), StandardCharsets.US_ASCII);
            loadFromJson(json);
        } catch (IOException e) {
            throw new ConfigurationException("Flower Shop configuration failed: " + e.getMessage());
        }
    }
}
