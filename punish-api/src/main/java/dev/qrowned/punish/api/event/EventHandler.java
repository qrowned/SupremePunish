package dev.qrowned.punish.api.event;

import dev.qrowned.punish.api.amqp.io.BinaryReadBuffer;
import org.jetbrains.annotations.NotNull;

public interface EventHandler {

    void receive(@NotNull BinaryReadBuffer binaryReadBuffer);

    void call(@NotNull AbstractPunishEvent event);

    void registerEventAdapter(EventAdapter... eventAdapters);

}
