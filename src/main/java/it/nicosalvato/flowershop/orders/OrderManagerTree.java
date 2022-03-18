package it.nicosalvato.flowershop.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderManagerTree {

    private static HashMap<String, List<Bundle>> products;
    static {
        products = new HashMap<>();
        List<Bundle> l = new ArrayList<>();
        l.add(new Bundle(10, 12.99));
        l.add(new Bundle(5, 6.99));
        products.put("R12", l);
    }

    public String processOrder(InputStream order) {
        String line = new BufferedReader(
                new InputStreamReader(order, StandardCharsets.UTF_8))
                .lines().findFirst().orElseThrow();

        String[] items = line.split(" ");
        int orderSize = Integer.parseInt(items[0]);
        String productCode = items[1];

        List<Bundle> sortedBundles = products.get(productCode).stream()
                .sorted((b1, b2) -> Integer.compare(b2.getBundleSize(), b1.getBundleSize()))
                .toList();
        System.out.println("Sorted bundles: " + sortedBundles.stream().map(Bundle::getBundleSize).map(String::valueOf).collect(Collectors.joining(", ")));

        int[] tree = ArrayUtils.addAll(new int[] {orderSize}, buildTree(orderSize, 0, sortedBundles));
        Map<Integer, Long> bundlesCount = IntStream.range(0, tree.length)
                .filter(i -> i % 2 == 1)
                .mapToObj(i -> tree[i])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        DeliveryItem deliveryItem = createDeliveredItem(bundlesCount, productCode);
        return deliveryItem.prettyPrint();
    }

    private int[] buildTree(int amount, int bundleIdx, List<Bundle> bundles) {
        int bundleSize = bundles.get(bundleIdx).getBundleSize();
        int remainder = amount - bundleSize;
        System.out.println("Amount: " + amount + ", bundleSize: " + bundleSize + ", remainder: " + remainder);
        if (remainder == 0) {
            // Esamina le foglie, trovato il risultato
            int[] tree = {bundleSize, remainder};
            return tree;
        }

        if (remainder < bundleSize) {
            if (bundleIdx < bundles.size() - 1) {
                // Test the next bundle option
                int[] subtree = buildTree(remainder, ++bundleIdx, bundles);
                if (subtree != null) {
                    // Solution found, join the array and return
                    int[] tree = {bundleSize, remainder};
                    return ArrayUtils.addAll(tree, subtree);
                } else {
                    // Rebuild current tree using next bundle option
                    return buildTree(amount, ++bundleIdx, bundles);
                }
            } else {
                // No more bundle options, reach the first level where previous bundle was involved and rebuild a
                // subtree with different bundle option
                return null;
            }
        }

        int[] tree = {bundles.get(bundleIdx).getBundleSize(), remainder};
        return ArrayUtils.addAll(tree, buildTree(remainder, bundleIdx, bundles));
    }

    private DeliveryItem createDeliveredItem(Map<Integer, Long> bundlesCount, String productCode) {
        List<ItemBundle> itemBundles = bundlesCount.entrySet()
                .stream()
                .map(entry -> new ItemBundle(Math.toIntExact(entry.getValue()), products.get(productCode).stream().filter(bundle -> bundle.getBundleSize() == entry.getKey()).findFirst().orElseThrow())).toList();
        return new DeliveryItem(productCode, itemBundles);
    }

    @Getter
    @AllArgsConstructor
    static class Bundle {
        private int bundleSize;
        private double price;
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
            StringBuilder sb = new StringBuilder(this.getAmount() + " " + code + " $" + this.getTotal());
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
            return " - " + amount + " x " + bundle.getBundleSize() + " $" + (bundle.getPrice() * amount);
        }
    }
}
