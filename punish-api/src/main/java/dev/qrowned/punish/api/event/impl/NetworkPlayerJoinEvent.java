package dev.qrowned.punish.api.event.impl;

import dev.qrowned.punish.api.event.AbstractPunishEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class NetworkPlayerJoinEvent extends AbstractPunishEvent {

    private final UUID uuid;
    private final String name;
    private final long time;

}
