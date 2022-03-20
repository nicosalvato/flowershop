package it.nicosalvato.flowershop.delivery.services;

import it.nicosalvato.flowershop.delivery.BundleDelivery;
import it.nicosalvato.flowershop.delivery.ProductDelivery;
import it.nicosalvato.flowershop.products.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.products.repositories.ProductRepository;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProductDeliveryService {
    private ProductDeliveryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static class SingletonHolder {
        public static final ProductDeliveryService instance = new ProductDeliveryService(new InMemoryProductRepository());
    }

    public static ProductDeliveryService getInstance() {
        return SingletonHolder.instance;
    }

    private ProductRepository productRepository;

    public ProductDelivery createProductDelivery(Map<Integer, Long> bundlesCount, String productCode) {
        System.out.println("Bundle count: " + ArrayUtils.toString(bundlesCount));
        List<BundleDelivery> itemBundles = bundlesCount.entrySet()
                .stream()
                .map(entry -> new BundleDelivery(Math.toIntExact(entry.getValue()), productRepository.findAllByProductCode(productCode).stream()
                        .filter(bundle -> bundle.getBundleSize() == entry.getKey())
                        .findFirst()
                        .orElseThrow()))
                .sorted(Comparator.comparing(BundleDelivery::getBundle))
                .toList();
        return new ProductDelivery(productCode, itemBundles);
    }
}
