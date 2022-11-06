package dev.qrowned.punish.api.event;

public abstract class EventAdapter<T extends AbstractPunishEvent> {

    public void handleReceive(T event) {

    }

    public void handleSend(T event) {

    }

}
