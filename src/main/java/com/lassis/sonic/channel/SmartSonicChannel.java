package com.lassis.sonic.channel;

import com.lassis.sonic.command.SonicCommand;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartSonicChannel implements SonicChannel {
    private static final Logger LOG = LoggerFactory.getLogger(SmartSonicChannel.class);

    private final Map<Mode, SonicChannel> channels = new ConcurrentHashMap<>();

    private final Function<Mode, SonicChannel> factory;

    public SmartSonicChannel(Function<Mode, SonicChannel> factory) {
        this.factory = factory;
    }

    @Override
    public <T> T send(SonicCommand<T> sonicCommand) {
        return channels.computeIfAbsent(sonicCommand.mode(), factory).send(sonicCommand);
    }

    @Override
    public boolean isValid() {
        for (var it : channels.entrySet()) {
            if (!it.getValue().isValid()) {
                LOG.debug("channel to mode {} is invalid", it.getKey());
                return false;
            }
        }

        return true;
    }

    @Override
    public void close() {
        channels.forEach((mode, ch) -> {
            ch.close();
            LOG.debug("channel to mode {} was closed", mode);
        });
    }

}
