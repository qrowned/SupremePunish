package dev.qrowned.punish.common.amqp.io;

import dev.qrowned.punish.api.amqp.exception.ReturningExceptionRunnable;
import dev.qrowned.punish.api.amqp.io.BinaryReadBuffer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommonBinaryReadBuffer implements BinaryReadBuffer {

    private final DataInputStream inputStream;

    public CommonBinaryReadBuffer(final byte[] data) {
        this.inputStream = new DataInputStream(new ByteArrayInputStream(data));
    }

    @Override
    public byte[] readBytes() {
        return this.doWithoutException(this::read);
    }

    @Override
    public String readString() {
        return this.doWithoutException(() -> new String(this.read(), StandardCharsets.UTF_8));
    }

    @Override
    public UUID readUUID() {
        return this.doWithoutException(() -> new UUID(this.inputStream.readLong(), this.inputStream.readLong()));
    }

    @Override
    public boolean readBoolean() {
        return this.doWithoutException(this.inputStream::readBoolean);
    }

    @Override
    public int readInt() {
        return this.doWithoutException(this.inputStream::readInt);
    }

    @Override
    public long readLong() {
        return this.doWithoutException(this.inputStream::readLong);
    }

    @Override
    public float readFloat() {
        return this.doWithoutException(this.inputStream::readFloat);
    }

    @Override
    public double readDouble() {
        return this.doWithoutException(this.inputStream::readDouble);
    }

    @Override
    public <T> T readObject() {
        return this.doWithoutException(() -> {
            final byte[] bytes = this.read();
            try (final ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
                try (final ObjectInputStream ois = new ObjectInputStream(bis)) {
                    return (T) ois.readObject();
                }
            }
        });
    }

    private <T> T doWithoutException(final ReturningExceptionRunnable<T> task) {
        try {
            return task.run();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private byte[] read() throws IOException {
        final byte[] bytes = new byte[this.inputStream.readInt()];
        final int read = inputStream.read(bytes);
        if (read != bytes.length)
            throw new RuntimeException("The read byte length is not equal to the expected byte length");
        return bytes;
    }

}
