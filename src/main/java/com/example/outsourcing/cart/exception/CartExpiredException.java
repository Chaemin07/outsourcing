package com.example.outsourcing.cart.exception;

public class CartExpiredException extends RuntimeException {
    public CartExpiredException(String message) {
        super(message);
    }
}
