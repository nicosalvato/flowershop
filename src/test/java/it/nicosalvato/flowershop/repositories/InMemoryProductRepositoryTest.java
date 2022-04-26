package it.nicosalvato.flowershop.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.nicosalvato.flowershop.pojos.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryProductRepositoryTest {

    ProductRepository productRepository = InMemoryProductRepository.getInstance();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        String json = """
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
        List<Product> products = new ObjectMapper().readValue(json, new TypeReference<List<Product>>() {});
        products.forEach(product -> productRepository.save(product));
    }

    @Test
    void testCount() {
        assertEquals(3, productRepository.count());
    }

    @Test
    void testFindByCode() {
        Product product = productRepository.findByCode("T58");
        assertEquals("Tulips", product.getName());
        assertEquals(3, product.getBundles().size());
    }

    @Test
    void findAllBundlesByProductCode() {
        var bundles = productRepository.findAllBundlesByProductCode("T58");
        assertEquals(3, bundles.size());
        assertEquals(5.95, bundles.stream().filter(b -> b.getBundleSize() == 3).findFirst().get().getPrice());

        bundles = productRepository.findAllBundlesByProductCode("R12");
        assertEquals(2, bundles.size());
    }

    @Test
    void testClear() {
        productRepository.clear();
        assertEquals(0, productRepository.count());
    }
}