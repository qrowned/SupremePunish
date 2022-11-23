package dev.qrowned.punish.api.event.impl;

import dev.qrowned.punish.api.event.AbstractPunishEvent;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class PlayerPunishEvent extends AbstractPunishEvent {

    private final UUID target;
    private final UUID executor;

    private final Punishment punishment;
    private final PunishmentReason punishmentReason;

}
