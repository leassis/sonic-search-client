package com.lassis.sonic.command.control;

import com.lassis.sonic.command.SonicCommand;

public class ControlCommands {
    private ControlCommands() {}

    public static SonicCommand<Boolean> consolidate() {
        return new TriggerSonicCommand(TriggerSonicCommand.Action.CONSOLIDATE);
    }
}
