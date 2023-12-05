package com.lassis.sonic.channel;

import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.exception.SonicException;
import java.util.function.Function;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledSonicChannelFactory implements SonicChannelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PooledSonicChannelFactory.class);

    private final GenericKeyedObjectPool<Mode, SonicChannel> connectionPool;

    public PooledSonicChannelFactory(Function<Mode, SonicChannel> factory, int maxTotal, int maxIdlePerKey, int minIdlePerKey) {
        var poolConfig = new GenericKeyedObjectPoolConfig<SonicChannel>();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdlePerKey(maxIdlePerKey);
        poolConfig.setMinIdlePerKey(minIdlePerKey);
//        poolConfig.setTestOnBorrow(true);

        this.connectionPool = new GenericKeyedObjectPool<>(new SonicChannelPooledObjectFactory(factory), poolConfig);
    }

    @Override
    public SonicChannel sonicChannel() {
        return new NoOpCloseSonicChannel(connectionPool);
    }

    private static class SonicChannelPooledObjectFactory extends BaseKeyedPooledObjectFactory<Mode, SonicChannel> {
        private static final Logger LOG = LoggerFactory.getLogger(SonicChannelPooledObjectFactory.class);

        private final Function<Mode, SonicChannel> factory;

        public SonicChannelPooledObjectFactory(Function<Mode, SonicChannel> factory) {
            this.factory = factory;
        }

        @Override
        public SonicChannel create(Mode mode) {
            var channel = factory.apply(mode);
            LOG.debug("creating connection {} to mode {}", channel, mode);
            return channel;
        }

        @Override
        public PooledObject<SonicChannel> wrap(SonicChannel sonicChannel) {
            var pooled = new DefaultPooledObject<>(sonicChannel);
            LOG.debug("wrapped connection {}", sonicChannel);
            return pooled;
        }

        @Override
        public void destroyObject(Mode key, PooledObject<SonicChannel> p) {
            p.getObject().close();
        }

        @Override
        public boolean validateObject(Mode key, PooledObject<SonicChannel> p) {
            return p.getObject().isValid();
        }
    }

    private static class NoOpCloseSonicChannel implements SonicChannel {
        private final GenericKeyedObjectPool<Mode, SonicChannel> connectionPool;

        public NoOpCloseSonicChannel(GenericKeyedObjectPool<Mode, SonicChannel> connectionPool) {
            this.connectionPool = connectionPool;
        }

        @Override
        public <T> T send(SonicCommand<T> sonicCommand) {
            SonicChannel channel;
            try {
                channel = connectionPool.borrowObject(sonicCommand.mode());
            } catch (Exception e) {
                throw new SonicException(e);
            }

            try {
                LOG.debug("connection {} borrowed", channel);
                return channel.send(sonicCommand);
            } finally {
                connectionPool.returnObject(sonicCommand.mode(), channel);
                LOG.debug("connection {} returned", channel);
            }
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public void close() {
            // noop
        }

    }
}
