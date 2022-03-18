package it.nicosalvato.flowershop.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class OrderManager {

    private static HashMap<String, Bundle> products;
    static {
        products = new HashMap<>();
        products.put("R12", new Bundle(10, 12.99));
        products.put("R12", new Bundle(5, 6.99));
    }

    public String processOrder(InputStream order) {
        new BufferedReader(
                new InputStreamReader(order, StandardCharsets.UTF_8))
                .lines().forEach(line -> {
                    String[] items = line.split(" ");
                    int price = Integer.parseInt(items[1]);

                    List<Bundle> sortedBundles = products.values().stream()
                            .sorted((b1, b2) -> Integer.compare(b2.getAmount(), b1.getAmount()))
                            .toList();

                    int[] bundleAmounts = sortedBundles.stream().mapToInt(Bundle::getAmount).toArray();
                    process(price, bundleAmounts, 0);
                });
        return null;
    }

    private int[][] process(int amount, int[] bundleAmounts, int i) {

        int q = amount / bundleAmounts[i];
        int r = amount % bundleAmounts[i];
        if (r == 0)
            return new int[][] {{bundleAmounts[i], q}};
        else if (r > 0 && i == bundleAmounts.length - 1)
            return null;
        else
            return process(r, bundleAmounts, ++i);
    }

    @Getter
    @AllArgsConstructor
    static class Bundle {
        private int amount;
        private double price;
    }
}
