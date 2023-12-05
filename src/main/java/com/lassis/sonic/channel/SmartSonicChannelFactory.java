package com.lassis.sonic.channel;

import java.util.function.Function;

public class SmartSonicChannelFactory implements SonicChannelFactory {
    private final Function<Mode, SonicChannel> factory;

    public SmartSonicChannelFactory(Function<Mode, SonicChannel> factory) {
        this.factory = factory;
    }

    @Override
    public SonicChannel sonicChannel() {
        return new SmartSonicChannel(factory);
    }
}
