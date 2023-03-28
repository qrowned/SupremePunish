package dev.qrowned.punish.common.amqp;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dev.qrowned.punish.api.amqp.ChannelHandler;
import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.amqp.channel.PubSubChannel;
import dev.qrowned.punish.api.amqp.io.BinaryWriteBufferFiller;
import dev.qrowned.punish.api.amqp.listener.PubSubListener;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.common.config.RabbitMqConfig;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CommonPubSubProvider implements PubSubProvider {

    private final List<ChannelHandler> channelHandlers = new ArrayList<>();

    private final PluginLogger logger;
    private final Connection connection;

    @SneakyThrows
    public CommonPubSubProvider(RabbitMqConfig config, PluginLogger pluginLogger) {
        this.logger = pluginLogger;

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(config.getHost());
        connectionFactory.setPort(config.getPort());
        connectionFactory.setUsername(config.getUsername());
        connectionFactory.setPassword(config.getPassword());
        connectionFactory.setConnectionTimeout(config.getConnectionTimeout());
        connectionFactory.setVirtualHost(config.getVirtualHost());

        pluginLogger.info("Trying to connect to " + connectionFactory.getHost() + ":" + connectionFactory.getPort() + " - " + connectionFactory.getUsername() + ":" + connectionFactory.getPassword());
        this.connection = connectionFactory.newConnection();
    }

    @Override
    public void registerListener(@NotNull PubSubListener pubSubListener) {
        PubSubChannel pubSubChannel = pubSubListener.getClass().getAnnotation(PubSubChannel.class);
        if (pubSubChannel == null) return;
        this.subscribe(pubSubChannel.value(), pubSubListener);
    }

    @Override
    public void publish(@NotNull String channel, @NotNull BinaryWriteBufferFiller binaryWriteBufferFiller) {
        ChannelHandler channelHandler = getChannelHandler(channel);
        if (channelHandler == null) {
            this.logger.warn("Failed to send message via rabbitmq. No ChannelHandler found for channel " + channel);
            return;
        }

        channelHandler.publishBinary(binaryWriteBufferFiller);
    }

    @Override
    public void subscribe(@NotNull String channelName, @NotNull PubSubListener pubSubListener) {
        ChannelHandler channelHandler = getChannelHandler(channelName);
        if (channelHandler == null) {
            this.logger.warn("Failed to subscribe to rabbitmq channel. No ChannelHandler found for channel " + channelName);
            return;
        }
        channelHandler.addListener(pubSubListener);
    }

    private ChannelHandler getChannelHandler(String channelName) {
        return this.channelHandlers.stream().filter(channelHandler -> channelHandler.getChannelName().equals(channelName)).findFirst().orElseGet(() -> {
            try {
                return new CommonChannelHandler(channelName, this.connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public @NotNull Collection<ChannelHandler> getChannelHandlers() {
        return this.channelHandlers;
    }

}
