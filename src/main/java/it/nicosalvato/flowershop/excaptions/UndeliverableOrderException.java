package it.nicosalvato.flowershop.excaptions;

public class UndeliverableOrderException extends RuntimeException {
    public UndeliverableOrderException(String msg) {
        super(msg);
    }
}
