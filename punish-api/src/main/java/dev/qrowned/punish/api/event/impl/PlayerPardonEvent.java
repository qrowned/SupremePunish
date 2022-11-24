package dev.qrowned.punish.api.event.impl;

import dev.qrowned.punish.api.event.AbstractPunishEvent;
import dev.qrowned.punish.api.punish.Punishment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class PlayerPardonEvent extends AbstractPunishEvent {

    private final UUID target;
    private final UUID executor;

    private final Punishment punishment;

}
