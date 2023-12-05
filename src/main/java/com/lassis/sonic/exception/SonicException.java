package com.lassis.sonic.exception;

public class SonicException extends RuntimeException{
    public SonicException(Throwable t) {
        super(t);
    }

    public SonicException(String msg) {
        super(msg);
    }
}
