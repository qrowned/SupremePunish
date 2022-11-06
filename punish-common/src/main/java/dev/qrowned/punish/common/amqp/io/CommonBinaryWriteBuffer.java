package dev.qrowned.punish.common.amqp.io;

import dev.qrowned.punish.api.amqp.exception.ExceptionRunnable;
import dev.qrowned.punish.api.amqp.io.BinaryWriteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommonBinaryWriteBuffer implements BinaryWriteBuffer {

    private final ByteArrayOutputStream outputStream;
    private final DataOutputStream buffer;

    public CommonBinaryWriteBuffer() {
        this.outputStream = new ByteArrayOutputStream();
        this.buffer = new DataOutputStream(this.outputStream);
    }

    @Override
    public BinaryWriteBuffer writeBytes(final byte[] data) {
        this.doWithoutException(() -> this.write(data));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeString(final String string) {
        this.doWithoutException(() -> this.write(string.getBytes(StandardCharsets.UTF_8)));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeUUID(final UUID uuid) {
        this.doWithoutException(() -> {
            this.buffer.writeLong(uuid.getMostSignificantBits());
            this.buffer.writeLong(uuid.getLeastSignificantBits());
        });
        return this;
    }

    @Override
    public BinaryWriteBuffer writeInt(final int i) {
        this.doWithoutException(() -> this.buffer.writeInt(i));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeLong(final long l) {
        this.doWithoutException(() -> this.buffer.writeLong(l));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeFloat(final float f) {
        this.doWithoutException(() -> this.buffer.writeFloat(f));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeDouble(final double d) {
        this.doWithoutException(() -> this.buffer.writeDouble(d));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeBoolean(final boolean b) {
        this.doWithoutException(() -> this.buffer.writeBoolean(b));
        return this;
    }

    @Override
    public BinaryWriteBuffer writeObject(final Object object) {
        this.doWithoutException(() -> {
            try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                try (final ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                    oos.writeObject(object);
                    oos.flush();
                    write(bos.toByteArray());
                }
            }
        });
        return this;
    }

    @Override
    public byte[] toByteArray() {
        return this.outputStream.toByteArray();
    }

    private void doWithoutException(final ExceptionRunnable task) {
        try {
            task.run();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void write(final byte[] data) throws IOException {
        this.buffer.writeInt(data.length);
        for (final byte b : data)
            this.buffer.writeByte(b);
    }

}
