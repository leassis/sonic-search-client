package com.lassis.sonic.channel;

import com.lassis.sonic.command.SonicCommand;
import com.lassis.sonic.command.global.GlobalCommands;
import com.lassis.sonic.exception.SonicException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSonicChannel implements SonicChannel {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleSonicChannel.class);

    private static final String EXPECTED_CONNECTED = "CONNECTED";
    private static final int START_BUFFER_SIZE = 1024;

    private final UUID uuid = UUID.randomUUID();
    private final InetSocketAddress endpoint;
    private final Socket socket;
    private final BufferedWriter writer;
    private final BufferedReader reader;
    private final Mode mode;

    public SimpleSonicChannel(InetSocketAddress endpoint,
                              Duration connectionTimeout,
                              Duration readTimeout,
                              Mode mode,
                              String password) {
        try {
            this.mode = mode;
            this.endpoint = endpoint;

            this.socket = new Socket();
            this.socket.connect(endpoint, (int) connectionTimeout.toMillis());
            this.socket.setSoTimeout((int) readTimeout.toMillis());
            ensureConnected(this.socket);

            var bufferSize = start(this.socket, mode, password);
            this.reader = getReader(this.socket, bufferSize);
            this.writer = getWriter(this.socket, bufferSize);
        } catch (IOException e) {
            throw new SonicException(e);
        }
    }

    @Override
    public synchronized <T> T send(SonicCommand<T> sonicCommand) {
        try {
            return send(sonicCommand, this.writer, this.reader);
        } catch (IOException e) {
            throw new SonicException(e);
        }
    }

    @Override
    public synchronized boolean isValid() {
        return send(GlobalCommands.ping(mode));
    }

    @Override
    public synchronized void close() {
        SimpleSonicChannel.this.send(GlobalCommands.quit(mode));
        close(writer, reader, socket);
    }

    private void close(Closeable... closeables) {
        for (var it : closeables) {
            if (Objects.nonNull(it)) {
                try {
                    it.close();
                } catch (IOException e) {
                    LOG.error("cannot close resource", e);
                }
            }
        }

    }

    private BufferedReader getReader(Socket socket, int bufferSize) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()), bufferSize);
    }

    private BufferedWriter getWriter(Socket socket, int bufferSize) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), bufferSize);
    }

    private int start(Socket socket, Mode mode, String password) throws IOException {
        return SimpleSonicChannel.this.send(
            GlobalCommands.start(mode, password), getWriter(socket, START_BUFFER_SIZE), getReader(socket, START_BUFFER_SIZE)
        );
    }

    private void ensureConnected(Socket socket) throws IOException {
        var innerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        var line = innerReader.readLine();
        if (!line.startsWith(EXPECTED_CONNECTED)) {
            throw new SonicException("Connection failed " + line);
        }
    }

    private <T> T send(SonicCommand<T> sonicCommand, BufferedWriter writer, BufferedReader reader) throws IOException {
        var content = sonicCommand.getContent();
        LOG.debug("sending command {} to {}", content, this);
        writer.write(content);
        writer.write("\r\n");
        writer.flush();

        var result = sonicCommand.parseResult(reader);
        LOG.debug("result command is {} from {}", result, this);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleSonicChannel{" +
               "endpoint=" + endpoint +
               ", mode=" + mode +
               ", channelId=" + uuid +
               '}';
    }
}
