package com.lassis.sonic.command.ingest;


import com.lassis.sonic.command.Partition;
import com.lassis.sonic.command.SonicCommand;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Locale;

public final class IngestCommands {
    private IngestCommands() {}

    public static SonicCommand<Boolean> push(@Nonnull Partition partition, @Nonnull String id, @Nonnull String content) {
        return push(partition, id, content, null);
    }

    public static SonicCommand<Boolean> push(@Nonnull Partition partition, @Nonnull String id, @Nonnull String content, @Nullable Locale locale) {
        return new PushSonicCommand(partition, id, content, locale);
    }

    public static SonicCommand<Integer> count(@Nonnull String collection) {
        return count(collection, null, null);
    }

    public static SonicCommand<Integer> count(@Nonnull Partition partition) {
        return count(partition.collection(), partition.bucket(), null);
    }

    public static SonicCommand<Integer> count(@Nonnull Partition partition, @Nonnull String object) {
        return count(partition.collection(), partition.bucket(), object);
    }

    public static SonicCommand<Integer> count(@Nonnull String collection, @Nullable String bucket, @Nullable String object) {
        return new CountSonicCommand(collection, bucket, object);
    }

    public static SonicCommand<Integer> flush(@Nonnull String collection) {
        return flush(collection, null, null);
    }

    public static SonicCommand<Integer> flush(@Nonnull Partition partition) {
        return flush(partition.collection(), partition.bucket(), null);
    }

    public static SonicCommand<Integer> flush(@Nonnull Partition partition, @Nonnull String object) {
        return flush(partition.collection(), partition.bucket(), object);
    }

    public static SonicCommand<Integer> flush(@Nonnull String collection, @Nullable String bucket, @Nullable String object) {
        return new FlushSonicCommand(collection, bucket, object);
    }
}
