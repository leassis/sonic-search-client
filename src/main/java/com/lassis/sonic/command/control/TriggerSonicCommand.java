
package com.lassis.sonic.command.control;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.exception.SonicCommandException;
import jakarta.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

class TriggerSonicCommand implements SonicCommand<Boolean> {
    private static final String EXPECTED_RESULT = "OK";

    private final Action action;

    TriggerSonicCommand(@Nonnull Action action) {
        Objects.requireNonNull(action);

        this.action = action;
    }

    @Override
    public Boolean parseResult(BufferedReader reader) throws IOException {
        var line = reader.readLine();
        if (Objects.equals(line, EXPECTED_RESULT)) {
            return true;
        }
        throw new SonicCommandException(getContent(), line);
    }

    public String getContent() {
        return "TRIGGER" + action;
    }

    @Override
    public Mode mode() {
        return Mode.CONTROL;
    }

    enum Action {
        CONSOLIDATE("consolidate");

        private final String name;

        Action(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
