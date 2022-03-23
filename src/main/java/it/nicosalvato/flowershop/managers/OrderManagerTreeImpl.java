package it.nicosalvato.flowershop.managers;

import it.nicosalvato.flowershop.pojos.delivery.delivery.ProductDelivery;
import it.nicosalvato.flowershop.pojos.products.Bundle;
import it.nicosalvato.flowershop.repositories.InMemoryProductRepository;
import it.nicosalvato.flowershop.repositories.ProductRepository;
import it.nicosalvato.flowershop.services.ProductDeliveryService;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderManagerTreeImpl implements OrderManager {

    private static final ProductRepository repository = InMemoryProductRepository.getInstance();
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

        int[] sortedBundleSizeList = repository.findAllByProductCode(productCode).stream().sorted().mapToInt(Bundle::getBundleSize).toArray();
        int[] tree = buildTree(new int[] {sortedBundleSizeList[0], orderSize}, sortedBundleSizeList);
        Map<Integer, Long> bundlesCount = IntStream.range(0, tree.length)
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> tree[i])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        ProductDelivery productDelivery = productDeliveryService.createProductDelivery(bundlesCount, productCode);
        return productDelivery.prettyPrint();
    }

     private int[] buildTree(int[] tree, int[] sortedBundleSizeList) {
        int level = tree.length / 2 - 1;
        int bundleSize = tree[level * 2];
        int amount = tree[level * 2 + 1];
        System.out.println(Arrays.toString(tree));

        if (amount == bundleSize) {
            /*
             * Found correct result;
             */
            return tree;
        }

        if (amount < bundleSize) {
            if (ArrayUtils.indexOf(sortedBundleSizeList, bundleSize) == sortedBundleSizeList.length - 1) {
                /*
                 * No more options left on the current level, climb the tree backward if possible
                 */
                if (level == 0)
                    return new int[] {};

                /*
                 * Find index of current bundleSize in the array and start again from
                 * [9, 13, 3, 4, 3, 1] --> [9, 13]
                 */
                int[] newTree = Arrays.copyOfRange(tree, 0, ArrayUtils.indexOf(tree, bundleSize));
                /*
                 * Try next bundleSize option
                 * [9, 13] --> [5, 13]
                 */
                newTree[newTree.length - 2] = getNextBundleSize(newTree[newTree.length - 2], sortedBundleSizeList);
                return buildTree(newTree, sortedBundleSizeList);
            } else {
                /*
                 * Try next available bundleSize
                 */
                int[] newTree = Arrays.copyOf(tree, tree.length);
                newTree[newTree.length - 2] = getNextBundleSize(newTree[newTree.length - 2], sortedBundleSizeList);
                return buildTree(newTree, sortedBundleSizeList);
            }
        }

        int[] newTree = Arrays.copyOf(tree, tree.length + 2);
        newTree[newTree.length - 2] = bundleSize;
        newTree[newTree.length - 1] = amount - bundleSize;
        return buildTree(newTree, sortedBundleSizeList);
    }

    private int getNextBundleSize(int currentBundleSize, int[] sortedBundleSizeList) {
        return sortedBundleSizeList[ArrayUtils.indexOf(sortedBundleSizeList, currentBundleSize) + 1];
    }
}
