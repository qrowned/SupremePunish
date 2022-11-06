package dev.qrowned.punish.common.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.amqp.channel.PubSubChannel;
import dev.qrowned.punish.api.amqp.io.BinaryReadBuffer;
import dev.qrowned.punish.api.amqp.listener.PubSubListener;
import dev.qrowned.punish.api.event.AbstractPunishEvent;
import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.logger.PluginLogger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@PubSubChannel(value = CommonEventHandler.CHANNEL_NAME)
@RequiredArgsConstructor
public final class CommonEventHandler implements EventHandler, PubSubListener {

    public static final String CHANNEL_NAME = "event";
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    private final List<EventAdapter> eventAdapters = new ArrayList<>();

    private final PluginLogger pluginLogger;
    private final PubSubProvider pubSubProvider;

    @Override
    @SneakyThrows
    public void receive(@NotNull BinaryReadBuffer binaryReadBuffer) {
        String className = binaryReadBuffer.readString();
        String s = binaryReadBuffer.readString();
        Class<?> eventClass = Class.forName(className);
        AbstractPunishEvent abstractPunishEvent = (AbstractPunishEvent) GSON.fromJson(s, eventClass);

        this.getEventAdapters(eventClass).forEach(eventAdapter -> eventAdapter.handleReceive(abstractPunishEvent));
    }

    @Override
    public void call(@NotNull AbstractPunishEvent event) {
        this.pubSubProvider.publish(CHANNEL_NAME, buffer -> {
            buffer.writeString(event.getClass().getName())
                    .writeString(GSON.toJson(event));
        });
    }

    @Override
    public void registerEventAdapter(EventAdapter... eventAdapters) {
        this.eventAdapters.addAll(Arrays.asList(eventAdapters));
    }

    private List<EventAdapter> getEventAdapters(Class<?> eventClazz) {
        return this.eventAdapters.stream().filter(eventAdapter -> {
            Class<? extends EventAdapter> eventAdapterClass = eventAdapter.getClass();

            EventListener eventListener = eventAdapterClass.getAnnotation(EventListener.class);

            if (eventListener == null) {
                this.pluginLogger.warn("Could not handle event at " + eventAdapterClass.getSimpleName() + ".");
                return false;
            }
            return eventListener.clazz().equals(eventClazz);
        }).sorted(Comparator.comparingInt(value -> {
            EventListener eventListener = value.getClass().getAnnotation(EventListener.class);
            return eventListener.priority();
        })).toList();
    }

}
