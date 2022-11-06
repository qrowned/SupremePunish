package dev.qrowned.punish.api.amqp.io;

import java.util.UUID;

public interface BinaryReadBuffer {

    byte[] readBytes();

    String readString();

    UUID readUUID();

    boolean readBoolean();

    int readInt();

    long readLong();

    float readFloat();

    double readDouble();

    <T> T readObject();

}
