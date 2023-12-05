package com.lassis.sonic.channel;

import java.util.Locale;

public enum Mode {
    INGEST, SEARCH, CONTROL;

    private final String value;

    Mode() {
        this.value = this.name().toLowerCase(Locale.ROOT);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
