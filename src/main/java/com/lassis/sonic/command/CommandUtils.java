package com.lassis.sonic.command;

import java.util.Locale;
import java.util.Objects;

public class CommandUtils {
    private CommandUtils() {}

    public static String partition(Partition partition) {
        Objects.requireNonNull(partition);
        return " " + partition.collection() + " " + partition.bucket();
    }

    public static String limit(Integer limit) {
        return Objects.nonNull(limit) ? " LIMIT(" + limit + ")" : "";
    }

    public static String offset(Integer offset) {
        return Objects.nonNull(offset) ? " OFFSET(" + offset + ")" : "";
    }

    public static String locale(Locale locale) {
        return Objects.nonNull(locale) ? " LANG(" + locale.getDisplayName() + ")" : "";
    }

    public static String term(String term) {
        return Objects.nonNull(term) ? " \"" + term + "\"" : "";
    }
}
