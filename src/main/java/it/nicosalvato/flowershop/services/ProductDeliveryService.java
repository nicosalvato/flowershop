package it.nicosalvato.flowershop.services;

import it.nicosalvato.flowershop.pojos.delivery.BundleDelivery;
import it.nicosalvato.flowershop.pojos.delivery.ProductDelivery;
import it.nicosalvato.flowershop.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProductDeliveryService {

    private final ProductRepository productRepository;

    private ProductDeliveryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static class SingletonHolder {
        public static final ProductDeliveryService instance = new ProductDeliveryService(InMemoryProductRepository.getInstance());
    }

    public static ProductDeliveryService getInstance() {
        return SingletonHolder.instance;
    }


    public ProductDelivery createProductDelivery(String productCode, int orderSize, Map<Integer, Long> bundlesCount) {
        List<BundleDelivery> itemBundles = null;
        if (!bundlesCount.isEmpty()) {
            itemBundles = bundlesCount.entrySet()
                    .stream()
                    .map(entry -> new BundleDelivery(Math.toIntExact(entry.getValue()), productRepository.findAllByProductCode(productCode).stream()
                            .filter(bundle -> bundle.getBundleSize() == entry.getKey())
                            .findFirst()
                            .orElseThrow()))
                    .sorted(Comparator.comparing(BundleDelivery::getBundle))
                    .toList();
        }
        return new ProductDelivery(productCode, orderSize, itemBundles);
    }

    public ProductDelivery createProductDelivery(String productCode, int orderSize) {
        return new ProductDelivery(productCode, orderSize, new ArrayList<>(0));
    }
}
