package it.nicosalvato.flowershop.exceptions;

public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
