package it.nicosalvato.flowershop.services;

import it.nicosalvato.flowershop.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ConfigurationServiceTest {

    ConfigurationService configurationService = ConfigurationService.getInstance();
    ProductRepository productRepository = InMemoryProductRepository.getInstance();

    @AfterEach
    void cleanUp() {
        productRepository.clear();
    }

    @Test
    void testSimpleConfDeserialization() {
        String conf = """
                [
                  {
                    "name": "Roses",
                    "code": "R12"
                  }
                ]""";

        configurationService.loadFromJson(conf);
        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    void testConfDeserialization() {
        String conf = """
                [
                   {
                     "name": "Roses",
                     "code": "R12",
                     "bundles": [
                       {
                         "bundleSize": 3,
                         "price": 5.95
                       }
                     ]
                   }
                 ]""";

        configurationService.loadFromJson(conf);
        Assertions.assertEquals(1, productRepository.count());
        Assertions.assertEquals(1, productRepository.findByCode("R12").getBundles().size());
    }

    @Test
    void testFullConfDeserialization() {
        String conf = """
                [
                  {
                    "code": "R12",
                    "name": "Roses",
                    "bundles": [
                      {
                        "bundleSize": 5,
                        "price": 6.99
                      }, {
                        "bundleSize": 10,
                        "price": 12.99
                      }
                    ]
                  }, {
                  "code": "L09",
                  "name": "Lilies",
                  "bundles": [
                    {
                      "bundleSize": 3,
                      "price": 9.95
                    }, {
                      "bundleSize": 6,
                      "price": 16.95
                    }, {
                      "bundleSize": 9,
                      "price": 24.95
                    }
                  ]
                }, {
                  "code": "T58",
                  "name": "Tulips",
                  "bundles": [
                    {
                      "bundleSize": 3,
                      "price": 5.95
                    }, {
                      "bundleSize": 5,
                      "price": 9.95
                    }, {
                      "bundleSize": 9,
                      "price": 16.99
                    }
                  ]
                }
                ]""";

        configurationService.loadFromJson(conf);
        Assertions.assertEquals(3, productRepository.count());
        Assertions.assertEquals(2, productRepository.findByCode("R12").getBundles().size());
        Assertions.assertEquals(3, productRepository.findByCode("L09").getBundles().size());
        Assertions.assertEquals(3, productRepository.findByCode("T58").getBundles().size());
    }
}
