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

class FlushSonicCommand implements SonicCommand<Integer> {
    private static final Pattern PATTERN = Pattern.compile("^RESULT (\\d+)$");
    public static final String CMD_FLUSH_OBJECT = "FLUSHO ";
    public static final String CMD_FLUSH_BUCKET = "FLUSHB ";
    public static final String CMD_FLUSH_COLLECTION = "FLUSHC ";
    private final String collection;
    private final String bucket;
    private final String object;

    public FlushSonicCommand(@Nonnull String collection, @Nullable String bucket, @Nullable String object) {
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
        if (isNotEmpty(bucket) && isNotEmpty(object)) {
            return CMD_FLUSH_OBJECT + collection + valueOrEmpty(bucket) + valueOrEmpty(object);
        }

        if (isNotEmpty(bucket)) {
            return CMD_FLUSH_BUCKET + collection + valueOrEmpty(bucket);
        }

        return CMD_FLUSH_COLLECTION + collection;
    }

    @Override
    public Mode mode() {
        return Mode.INGEST;
    }


    public String valueOrEmpty(String value) {
        return Objects.nonNull(value) ? " " + value : "";
    }

    public boolean isNotEmpty(String value) {
        return Objects.nonNull(value) && !value.trim().isEmpty();
    }


}
