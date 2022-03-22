package it.nicosalvato.flowershop.products.services;

import it.nicosalvato.flowershop.products.Product;
import it.nicosalvato.flowershop.products.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.products.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ProductServiceTest {

    ProductService productService = ProductService.getInstance();
    ProductRepository productRepository = InMemoryProductRepository.getInstance();

    @AfterEach
    void cleanUp() {
        productRepository.clear();
    }

    @Test
    void testConfiguration1Deserialization() throws FileNotFoundException {
        String configuration = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/configuration_1.json"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        productService.readProductsFromJson(configuration);
        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    void testConfiguration2Deserialization() throws FileNotFoundException {
        String configuration = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/configuration_2.json"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        productService.readProductsFromJson(configuration);
        Assertions.assertEquals(1, productRepository.count());
        Assertions.assertEquals(1, productRepository.findByCode("R12").getBundles().size());
    }

    @Test
    void testConfiguration3Deserialization() throws FileNotFoundException {
        String configuration = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/configuration_3.json"),
                        StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        productService.readProductsFromJson(configuration);
        Assertions.assertEquals(3, productRepository.count());
        Assertions.assertEquals(2, productRepository.findByCode("R12").getBundles().size());
        Assertions.assertEquals(3, productRepository.findByCode("L09").getBundles().size());
        Assertions.assertEquals(3, productRepository.findByCode("T58").getBundles().size());
    }
}
