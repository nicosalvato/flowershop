package it.nicosalvato.flowershop.orders;

import it.medas.flowershop.products.Bundle;
import it.medas.flowershop.products.InMemoryProductRepository;
import it.medas.flowershop.products.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderManagerTree {

    private static final ProductRepository repository = new InMemoryProductRepository();

    public String processOrder(InputStream order) {
        String line = new BufferedReader(
                new InputStreamReader(order, StandardCharsets.UTF_8))
                .lines().findFirst().orElseThrow();

        String[] items = line.split(" ");
        int orderSize = Integer.parseInt(items[0]);
        String productCode = items[1];

        List<Bundle> sortedBundles = repository.findAllByProductCode(productCode).stream().sorted().toList();
        System.out.println("Sorted bundles: " + sortedBundles.stream().map(Bundle::getBundleSize).map(String::valueOf).collect(Collectors.joining(", ")));

        Map<Integer, Integer> levelIndexMap = new HashMap<>();
        levelIndexMap.put(0, 0);
        int[] tree = ArrayUtils.addAll(new int[] {orderSize}, buildTree(orderSize, 0, levelIndexMap, sortedBundles));
        Map<Integer, Long> bundlesCount = IntStream.range(0, tree.length)
                .filter(i -> i % 2 == 1)
                .mapToObj(i -> tree[i])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        bundlesCount.entrySet().forEach(System.out::print);
        DeliveryItem deliveryItem = createDeliveredItem(bundlesCount, productCode);
        return deliveryItem.prettyPrint();
    }

    private int[] buildTree(int amount, int level, Map<Integer, Integer> levelIndexMap, List<Bundle> bundles) {
        int bundleIdx = levelIndexMap.get(level);
        int bundleSize = bundles.get(bundleIdx).getBundleSize();
        int remainder = amount - bundleSize;
        System.out.println("Amount: " + amount + ", bundleSize: " + bundleSize + ", remainder: " + remainder + ", level: " + level);

        if (remainder == 0) {
            return new int[]{bundleSize, remainder};
        }

        if (remainder < 0) {
            if (bundleIdx == bundles.size() - 1) {
                // No more options on this level, go back to last level with options left
                Map.Entry<Integer, Integer> lastLeveWithHope = levelIndexMap.entrySet().stream().filter(e -> e.getValue() < bundles.size() - 1).findFirst().orElseThrow();
                int oldAmount = amount;
                for (int i = level - 1; i >= lastLeveWithHope.getKey(); i--) {
                    oldAmount += bundles.get(levelIndexMap.get(i)).getBundleSize();
                    System.out.println("Level: " + i + ", bundle size: " + bundles.get(levelIndexMap.get(i)).getBundleSize() + ", amount: " + oldAmount);
                }
                levelIndexMap.put(lastLeveWithHope.getKey(), lastLeveWithHope.getValue() + 1);
                return buildTree(oldAmount, lastLeveWithHope.getKey(), levelIndexMap, bundles);
            } else {
                levelIndexMap.put(level, bundleIdx + 1);
                return buildTree(amount, level, levelIndexMap, bundles);
            }
        }

        int[] tree = {bundles.get(bundleIdx).getBundleSize(), remainder};
        levelIndexMap.put(level + 1, bundleIdx);
        return ArrayUtils.addAll(tree, buildTree(remainder, level + 1, levelIndexMap, bundles));
    }

    private DeliveryItem createDeliveredItem(Map<Integer, Long> bundlesCount, String productCode) {
        List<ItemBundle> itemBundles = bundlesCount.entrySet()
                .stream()
                .map(entry -> new ItemBundle(Math.toIntExact(entry.getValue()), repository.findAllByProductCode(productCode).stream().filter(bundle -> bundle.getBundleSize() == entry.getKey()).findFirst().orElseThrow()))
                .sorted(Comparator.comparing(OrderManagerTree.ItemBundle::getBundle))
                .toList();
        return new DeliveryItem(productCode, itemBundles);
    }

    @Getter
    @AllArgsConstructor
    static class DeliveryItem {
        private String code;
        private List<ItemBundle> itemBundles;

        public double getTotal() {
            return itemBundles.stream()
                    .map(itemBundle -> itemBundle.getAmount() * itemBundle.getBundle().getPrice())
                    .reduce(0.0, Double::sum);
        }

        public int getAmount() {
            return itemBundles.stream()
                    .mapToInt(item -> item.getAmount() * item.bundle.getBundleSize())
                    .sum();
        }

        String prettyPrint() {
            StringBuilder sb = new StringBuilder(this.getAmount() + " " + code + " $" + new DecimalFormat("0.00").format(this.getTotal()));
            for (ItemBundle itemBundle: itemBundles) {
                sb.append("\n").append(itemBundle.prettyPrint());
            }
            return sb.toString();
        }
    }

    @Getter
    @AllArgsConstructor
    static class ItemBundle {
        private int amount;
        private Bundle bundle;

        String prettyPrint() {
            return " - " + amount + " x " + bundle.getBundleSize() + " $" + new DecimalFormat("0.00").format(bundle.getPrice() * amount);
        }
    }
}
