package dev.qrowned.punish.api.event;

import org.jetbrains.annotations.NotNull;

public abstract class EventAdapter<T extends AbstractPunishEvent> {

    public void handleReceive(@NotNull T event) {

    }

    public void handleSend(@NotNull T event) {

    }

}
