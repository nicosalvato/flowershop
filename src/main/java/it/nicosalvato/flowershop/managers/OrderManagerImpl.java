package it.nicosalvato.flowershop.managers;

import it.nicosalvato.flowershop.exceptions.UndeliverableOrderException;
import it.nicosalvato.flowershop.pojos.products.Bundle;
import it.nicosalvato.flowershop.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.repositories.ProductRepository;
import it.nicosalvato.flowershop.services.ProductDeliveryService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is an efficient dynamic programming approach to the coin change problem.
 * Credits for this algorithm go to someone else.
 */
public class OrderManagerImpl implements OrderManager {

    private static final ProductRepository repository = InMemoryProductRepository.getInstance();
    private static final ProductDeliveryService productDeliveryService = ProductDeliveryService.getInstance();

    @Override
    public String processOrder(InputStream order) {
        return new BufferedReader(
                new InputStreamReader(order, StandardCharsets.UTF_8))
                .lines()
                .map(this::processOrderLine)
                .collect(Collectors.joining("\n"));
    }

    private String processOrderLine(String line) {
        String[] items = line.split(" ");
        int orderSize = Integer.parseInt(items[0]);
        String productCode = items[1];

        List<Bundle> sortedBundles = repository.findAllByProductCode(productCode).stream().sorted(Comparator.reverseOrder()).toList();
        int[] bundleItems;
        try {
            bundleItems = processOrderItem(orderSize,
                    sortedBundles.stream().map(Bundle::getBundleSize).mapToInt(Integer::intValue).toArray());
            Map<Integer, Long> bundlesCount = Arrays.stream(bundleItems)
                    .boxed()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            return productDeliveryService.createProductDelivery(productCode, orderSize, bundlesCount).prettyPrint();
        } catch (UndeliverableOrderException e) {
            return productDeliveryService.createProductDelivery(productCode, orderSize).prettyPrint(e.getMessage());
        }
    }

    public static int[] processOrderItem(int amount, int[] currencies) throws UndeliverableOrderException {

        /*
        int[] dp: stores at index i the number of ways in which amount i can be paid with given currencies (dp[0] = 1
        since there is nothing to pay there).

        ArrayList<String>[] payments: stores at index i all currency combinations that equals amount i.
         */
        int[] dp = new int[amount + 1];
        ArrayList<String>[] payments = new ArrayList[amount + 1];
        for (int i = 0; i < payments.length; i++) {
            payments[i] = new ArrayList<>();
        }

        dp[0] = 1;

        for (int currency : currencies) {
            for (int amt = 1; amt < dp.length; amt++) {
                if (amt - currency >= 0 && dp[amt - currency] != 0) {
                    /*
                    This is the case when (amount - currency) difference can actually be paid with some currency
                    combination explored in previous iterations.

                    Currencies required to pay current amount are those required to pay (amount - currency) plus current
                    currency.
                     */
                    dp[amt] += 1;
                    String payment = payments[amt - currency].size() > 0 ?
                            (payments[amt - currency].get(payments[amt - currency].size() - 1) + currency + " ")
                            : currency + " ";
                    payments[amt].add(payment);
                }
            }
        }

        return Arrays.stream(payments[amount]
                .stream()
                .min(Comparator.comparingInt(String::length))
                .orElseThrow(() -> {
                    throw new UndeliverableOrderException(DEFAULT_UNDELIVERABLE_PRODUCT_EX_MESSAGE);
                })
                .split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

    }
}
