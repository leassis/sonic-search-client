package com.lassis.sonic.command;

import com.lassis.sonic.channel.Mode;
import java.io.BufferedReader;
import java.io.IOException;

public interface SonicCommand<T> {
    T parseResult(BufferedReader reader) throws IOException;

    String getContent();

    Mode mode();
}
