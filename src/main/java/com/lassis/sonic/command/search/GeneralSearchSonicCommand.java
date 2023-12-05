package com.lassis.sonic.command.search;

import static com.lassis.sonic.command.CommandUtils.limit;
import static com.lassis.sonic.command.CommandUtils.locale;
import static com.lassis.sonic.command.CommandUtils.offset;
import static com.lassis.sonic.command.CommandUtils.partition;
import static com.lassis.sonic.command.CommandUtils.term;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.Partition;
import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.exception.SonicException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GeneralSearchSonicCommand implements SonicCommand<Collection<String>> {
    private static final Logger LOG = LoggerFactory.getLogger(GeneralSearchSonicCommand.class);
    private static final Pattern EVENT_PATTERN = Pattern.compile("^PENDING (.+)$");

    private final String command;
    private final Partition partition;
    private final String term;
    private final Integer limit;
    private final Integer offset;
    private final Locale locale;

    GeneralSearchSonicCommand(@Nonnull String command,
                              @Nonnull Partition partition,
                              @Nullable String term,
                              @Nullable Integer limit,
                              @Nullable Integer offset,
                              @Nullable Locale locale) {

        this.command = command;
        this.partition = partition;
        this.term = term;
        this.limit = limit;
        this.offset = offset;
        this.locale = locale;
    }

    @Override
    public Collection<String> parseResult(BufferedReader reader) throws IOException {
        var line = reader.readLine();
        var matcher = EVENT_PATTERN.matcher(line);
        String eventId;
        if (matcher.matches()) {
            eventId = matcher.group(1);
        } else {
            throw new SonicException("ERROR Querying " + line);
        }

        line = reader.readLine();
        var start = "EVENT " + command + " " + eventId;
        if (line.startsWith(start)) {
            var content = Arrays.asList(line.split(" "));
            return content.subList(3, content.size());
        }
        LOG.warn("Response {} does not match to id {}", line, eventId);
        return Collections.emptyList();
    }

    @Override
    public String getContent() {
        return command + partition(partition) + term(term) + limit(limit) + offset(offset) + locale(locale);
    }

    @Override
    public Mode mode() {
        return Mode.SEARCH;
    }
}
