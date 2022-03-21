package it.nicosalvato.flowershop.orders;

import it.nicosalvato.flowershop.delivery.ProductDelivery;
import it.nicosalvato.flowershop.delivery.services.ProductDeliveryService;
import it.nicosalvato.flowershop.products.Bundle;
import it.nicosalvato.flowershop.products.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.products.repositories.ProductRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EfficientOrderManager implements OrderManager {

    private static final ProductRepository repository = new InMemoryProductRepository();
    private static final ProductDeliveryService productDeliveryService = ProductDeliveryService.getInstance();

    @Override
    public String processOrder(InputStream order) {
        return new BufferedReader(
                new InputStreamReader(order, StandardCharsets.UTF_8))
                .lines().map(this::processOrderLine).collect(Collectors.joining("\n"));
    }

    private String processOrderLine(String line) {
        String[] items = line.split(" ");
        int orderSize = Integer.parseInt(items[0]);
        String productCode = items[1];

        List<Bundle> sortedBundles = repository.findAllByProductCode(productCode).stream().sorted().toList();
        int[] bundleItems = processOrderItem(orderSize,
                sortedBundles.stream().map(Bundle::getBundleSize).mapToInt(Integer::intValue).toArray());
        Map<Integer, Long> bundlesCount = Arrays.stream(bundleItems)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        ProductDelivery productDelivery = productDeliveryService.createProductDelivery(bundlesCount, productCode);
        return productDelivery.prettyPrint();
    }

    public static int[] processOrderItem(int amount, int[] currencies) {
        /*
         * dp array will contain the number of ways 'i'
         * amount can be paid using the given currencies,
         * therefore, we made dp of size amount+1 to have
         * an index = amount.
         */
        int[] dp = new int[amount + 1];
        ArrayList<String>[] payments = new ArrayList[amount + 1];
        for (int i = 0; i < payments.length; i++) {
            payments[i] = new ArrayList<>();
        }

        /*
         * positive basecase, when we have remaining amount = 0,
         * this means that we have found one way of paying the
         * initial amount.
         */
        dp[0] = 1;

        for (int currency : currencies) {
            for (int amt = 1; amt < dp.length; amt++) {
                if (amt - currency >= 0 && dp[amt - currency] != 0) {
                    dp[amt] += 1;
                    /*  we have made an array of arraylist of strings to
                     * store all the ways of paying the current amount,
                     *  therefore, the payments of current amount =
                     *  payments of (amt - currency) concatenated
                     *  with the current currency*/
                    String payment = payments[amt - currency].size() > 0 ?
                            (payments[amt - currency].get(payments[amt - currency].size() - 1) + currency + " ")
                            : currency + " ";
                    payments[amt].add(payment);
                }
            }
        }

        /*number of ways of paying given amount = dp[amount]*/
        System.out.println(dp[amount] + "\n" + payments[amount]);

        return Arrays.stream(payments[amount]
                .stream()
                .min(Comparator.comparingInt(String::length))
                .orElseThrow()
                .split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

    }
}
