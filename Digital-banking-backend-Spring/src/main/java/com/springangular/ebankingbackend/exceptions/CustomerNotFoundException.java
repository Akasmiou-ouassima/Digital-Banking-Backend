package com.springangular.ebankingbackend.exceptions;

public class CustomerNotFoundException extends Exception{ // RuntimeException est non surveillée
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
