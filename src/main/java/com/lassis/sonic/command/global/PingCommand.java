package com.lassis.sonic.command.global;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.SonicCommand;
import jakarta.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

record PingCommand(@Nonnull Mode mode) implements SonicCommand<Boolean> {

    @Override
    public Boolean parseResult(BufferedReader reader) throws IOException {
        return Objects.equals(reader.readLine(), "PONG");
    }

    @Override
    public String getContent() {
        return "PING";
    }
}
