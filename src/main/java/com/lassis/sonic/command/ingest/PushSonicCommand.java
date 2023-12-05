package com.lassis.sonic.command.ingest;

import static com.lassis.sonic.command.CommandUtils.locale;
import static com.lassis.sonic.command.CommandUtils.partition;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.Partition;
import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.exception.SonicCommandException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

class PushSonicCommand implements SonicCommand<Boolean> {
    private static final String EXPECTED_RESULT = "OK";

    private final Partition partition;
    private final String id;
    private final String content;
    private final Locale locale;

    PushSonicCommand(@Nonnull Partition partition, @Nonnull String id, @Nonnull String content, @Nullable Locale locale) {
        Objects.requireNonNull(partition);
        Objects.requireNonNull(id);
        Objects.requireNonNull(content);

        this.partition = partition;
        this.id = id;
        this.content = content;
        this.locale = locale;
    }

    @Override
    public Boolean parseResult(BufferedReader reader) throws IOException {
        var line = reader.readLine();
        if (Objects.equals(line, EXPECTED_RESULT)) {
            return true;
        }
        throw new SonicCommandException(getContent(), line);
    }

    @Override
    public String getContent() {
        return "PUSH" + partition(partition) + " " + id + " \"" + this.content + "\"" + locale(locale);
    }

    @Override
    public Mode mode() {
        return Mode.INGEST;
    }

}
