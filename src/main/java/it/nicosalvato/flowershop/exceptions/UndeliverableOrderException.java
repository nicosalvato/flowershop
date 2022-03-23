package it.nicosalvato.flowershop.exceptions;

public class UndeliverableOrderException extends RuntimeException {
    public UndeliverableOrderException(String msg) {
        super(msg);
    }
}
