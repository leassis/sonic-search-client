package com.lassis.sonic.exception;

public class SonicCommandException extends RuntimeException {
    public SonicCommandException(String command, String result) {
        super(command + " has invalid response: " + result);
    }
}
