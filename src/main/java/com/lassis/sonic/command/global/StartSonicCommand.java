package com.lassis.sonic.command.global;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.exception.SonicCommandException;
import jakarta.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

class StartSonicCommand implements SonicCommand<Integer> {
    private final Pattern pattern;
    private final Mode mode;
    private final String password;

    StartSonicCommand(@Nonnull Mode mode, @Nonnull String password) {
        Objects.requireNonNull(mode);
        Objects.requireNonNull(password);

        this.mode = mode;
        this.password = password;
        this.pattern = Pattern.compile("^STARTED " + mode.getValue() + ".*buffer\\((\\d+)\\).*$");
    }

    @Override
    public Integer parseResult(BufferedReader reader) throws IOException {
        var line = reader.readLine();
        var matcher = pattern.matcher(line);
        if (matcher.matches()) {
            var group = matcher.group(1);
            return Integer.valueOf(group);
        }
        throw new SonicCommandException(getContent(), line);
    }

    @Override
    public String getContent() {
        return "START " + mode + " " + password;
    }

    @Override
    public Mode mode() {
        return mode;
    }
}
