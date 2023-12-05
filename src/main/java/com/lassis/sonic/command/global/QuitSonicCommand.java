package com.lassis.sonic.command.global;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.SonicCommand;
import jakarta.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

 record QuitSonicCommand(@Nonnull Mode mode) implements SonicCommand<Boolean> {
    private static final String EXPECTED_RESULT = "ENDED quit";
    private static final String QUIT = "QUIT";

    @Override
    public Boolean parseResult(BufferedReader reader) throws IOException {
        return Objects.equals(reader.readLine(), EXPECTED_RESULT);
    }

    @Override
    public String getContent() {
        return QUIT;
    }

}
