package com.lassis.sonic.command.global;

import com.lassis.sonic.channel.Mode;
import com.lassis.sonic.command.SonicCommand;
import jakarta.annotation.Nonnull;

public class GlobalCommands {

    private GlobalCommands() {}

    public static SonicCommand<Boolean> ping(@Nonnull Mode mode) {
        return new PingCommand(mode);
    }

    public static SonicCommand<Boolean> quit(@Nonnull Mode mode) {
        return new QuitSonicCommand(mode);
    }

    public static SonicCommand<Integer> start(@Nonnull Mode mode, @Nonnull String password) {
        return new StartSonicCommand(mode, password);
    }

}
