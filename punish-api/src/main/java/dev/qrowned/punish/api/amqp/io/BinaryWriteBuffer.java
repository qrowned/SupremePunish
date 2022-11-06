package dev.qrowned.punish.api.amqp.io;

import java.util.UUID;

public interface BinaryWriteBuffer {

    BinaryWriteBuffer writeBytes(byte[] bytes);

    BinaryWriteBuffer writeString(String string);

    BinaryWriteBuffer writeUUID(UUID uuid);

    BinaryWriteBuffer writeBoolean(boolean b);

    BinaryWriteBuffer writeInt(int i);

    BinaryWriteBuffer writeLong(long l);

    BinaryWriteBuffer writeFloat(float f);

    BinaryWriteBuffer writeDouble(double d);

    BinaryWriteBuffer writeObject(Object o);

    byte[] toByteArray();

}
