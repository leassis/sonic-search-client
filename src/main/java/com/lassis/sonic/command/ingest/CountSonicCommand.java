package com.lassis.sonic.command.ingest;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.exception.SonicCommandException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

class CountSonicCommand implements SonicCommand<Integer> {
    private static final Pattern PATTERN = Pattern.compile("^RESULT (\\d+)$");
    private final String collection;
    private final String bucket;
    private final String object;

    public CountSonicCommand(@Nonnull String collection, @Nullable String bucket, @Nullable String object) {
        Objects.requireNonNull(collection);

        this.collection = collection;
        this.bucket = bucket;
        this.object = object;
    }

    @Override
    public Integer parseResult(BufferedReader reader) throws IOException {
        var line = reader.readLine();
        var matcher = PATTERN.matcher(line);
        if (matcher.matches()) {
            return Integer.valueOf(matcher.group(1));
        }

        throw new SonicCommandException(getContent(), line);
    }

    @Override
    public String getContent() {
        return "COUNT " + collection + valueOrEmpty(bucket) + valueOrEmpty(object);
    }

    @Override
    public Mode mode() {
        return Mode.INGEST;
    }


    public String valueOrEmpty(String value) {
        return Objects.nonNull(value) ? " " + value : "";
    }


}
