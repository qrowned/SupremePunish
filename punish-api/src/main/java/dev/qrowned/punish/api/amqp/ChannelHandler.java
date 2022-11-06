package dev.qrowned.punish.api.amqp;

import com.rabbitmq.client.Channel;
import dev.qrowned.punish.api.amqp.io.BinaryWriteBufferFiller;
import dev.qrowned.punish.api.amqp.listener.PubSubListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface ChannelHandler {

    @NotNull Channel getChannel();

    @NotNull String getChannelName();

    void subscribe() throws IOException;

    void addListener(PubSubListener listener);

    void publishBinary(BinaryWriteBufferFiller buffer);

}
