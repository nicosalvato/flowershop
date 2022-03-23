package it.nicosalvato.flowershop.excaptions;

public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
