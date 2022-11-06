package dev.qrowned.punish.common.amqp;

import com.rabbitmq.client.*;
import dev.qrowned.punish.api.amqp.ChannelHandler;
import dev.qrowned.punish.api.amqp.io.BinaryWriteBufferFiller;
import dev.qrowned.punish.api.amqp.listener.PubSubListener;
import dev.qrowned.punish.common.amqp.io.CommonBinaryReadBuffer;
import dev.qrowned.punish.common.amqp.io.CommonBinaryWriteBuffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class CommonChannelHandler implements ChannelHandler {

    private final Set<PubSubListener> listenerSet = new HashSet<>();

    private final String channelName;
    private final Channel channel;
    private final Consumer consumer;
    private boolean listenerSubscribed = false;

    public CommonChannelHandler(String channelName, Connection connection) throws IOException {
        this.channelName = channelName;
        this.channel = connection.createChannel();
        this.channel.exchangeDeclare(channelName, BuiltinExchangeType.FANOUT);

        this.consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                CompletableFuture.runAsync(() -> {
                    String bodyString = new String(body, StandardCharsets.UTF_8);
                    final byte[] data = Base64.getDecoder().decode(bodyString);

                    CommonBinaryReadBuffer buffer = new CommonBinaryReadBuffer(data);

                    listenerSet.forEach(pubSubListener -> pubSubListener.receive(buffer));
                });
            }
        };
    }

    @Override
    public @NotNull Channel getChannel() {
        return this.channel;
    }

    @Override
    public @NotNull String getChannelName() {
        return this.channelName;
    }

    @Override
    public void subscribe() throws IOException {
        if (this.listenerSubscribed) return;
        String queueName = channel.queueDeclare().getQueue();
        this.channel.queueBind(queueName, channelName, "");
        this.channel.basicConsume(queueName, true, this.consumer);
        this.listenerSubscribed = true;
    }

    @Override
    public void addListener(PubSubListener listener) {
        try {
            subscribe();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listenerSet.add(listener);
    }

    @Override
    public void publishBinary(BinaryWriteBufferFiller filler) {
        try {
            CommonBinaryWriteBuffer binaryWriteBuffer = new CommonBinaryWriteBuffer();
            filler.fill(binaryWriteBuffer);
            String encode = Base64.getEncoder().encodeToString(binaryWriteBuffer.toByteArray());
            this.channel.basicPublish(this.channelName, "", null, encode.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
