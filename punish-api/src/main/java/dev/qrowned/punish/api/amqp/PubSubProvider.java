package dev.qrowned.punish.api.amqp;

import dev.qrowned.punish.api.amqp.io.BinaryWriteBufferFiller;
import dev.qrowned.punish.api.amqp.listener.PubSubListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface PubSubProvider {

    void registerListener(@NotNull PubSubListener pubSubListener);

    void publish(@NotNull String channel, @NotNull BinaryWriteBufferFiller binaryWriteBufferFiller);

    void subscribe(@NotNull String channel, @NotNull PubSubListener pubSubListener);

    @NotNull Collection<ChannelHandler> getChannelHandlers();

}
