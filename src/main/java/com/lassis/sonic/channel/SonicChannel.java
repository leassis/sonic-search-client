package com.lassis.sonic.channel;

import com.lassis.sonic.command.SonicCommand;
import java.io.Closeable;

public interface SonicChannel extends Closeable {
    <T> T send(SonicCommand<T> sonicCommand);

    boolean isValid();

    void close();
}
