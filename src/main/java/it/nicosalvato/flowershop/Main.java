package it.nicosalvato.flowershop;

import it.nicosalvato.flowershop.exceptions.UndeliverableOrderException;
import it.nicosalvato.flowershop.managers.OrderManager;
import it.nicosalvato.flowershop.managers.OrderManagerImpl;
import it.nicosalvato.flowershop.services.ConfigurationService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main (String[] args) {
        if (args.length < 2) {
            printHelp();
            System.exit(1);
        }

        String flowerOrder = args[0];
        String confPath = args[1];

        ConfigurationService configurationService = ConfigurationService.getInstance();
        OrderManager orderManager = new OrderManagerImpl();

        try {
            System.out.println("Configuring products...");
            configurationService.loadFromFile(confPath);
            System.out.println("Processing order using " + orderManager.getClass().getSimpleName() + " class...");
            System.out.println(orderManager.processOrder(new FileInputStream(flowerOrder)));
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("Please provide a valid flower order path! (" + e.getMessage() + ")");
            System.exit(1);
        } catch (UndeliverableOrderException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Something went wrong! (" + e.getMessage() + ")");
            System.exit(1);
        }
    }

    private static void printHelp() {
        System.out.println("Usage: flowershop <order_filepath> <json_configuration_filepath>");
    }
}
