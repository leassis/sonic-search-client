package com.lassis.sonic.command.search;


import com.lassis.sonic.command.Partition;
import com.lassis.sonic.command.SonicCommand;

import java.util.Collection;
import java.util.Locale;

public final class SearchCommands {
    public static final String CMD_QUERY = "QUERY";
    public static final String CMD_LIST = "LIST";
    public static final String CMD_SUGGEST = "SUGGEST";

    private SearchCommands() {}

    public static SonicCommand<Collection<String>> query(Partition partition, String term) {
        return query(partition, term, null, null, null);
    }

    public static SonicCommand<Collection<String>> query(Partition partition, String term, Locale locale) {
        return query(partition, term, null, null, locale);
    }

    public static SonicCommand<Collection<String>> query(Partition partition, String term, Integer limit, Integer offset) {
        return query(partition, term, limit, offset, null);
    }

    public static SonicCommand<Collection<String>> query(Partition partition, String term, Integer limit, Integer offset, Locale locale) {
        return new GeneralSearchSonicCommand(CMD_QUERY, partition, term, limit, offset, locale);
    }

    public static SonicCommand<Collection<String>> suggest(Partition partition, String term) {
        return suggest(partition, term, null);
    }

    public static SonicCommand<Collection<String>> suggest(Partition partition, String term, Integer limit) {
        return new GeneralSearchSonicCommand(CMD_SUGGEST, partition, term, limit, null, null);
    }

    public static SonicCommand<Collection<String>> list(Partition partition) {
        return list(partition, null, null);
    }

    public static SonicCommand<Collection<String>> list(Partition partition, Integer limit, Integer offset) {
        return new GeneralSearchSonicCommand(CMD_LIST, partition, null, limit, offset, null);
    }

}
