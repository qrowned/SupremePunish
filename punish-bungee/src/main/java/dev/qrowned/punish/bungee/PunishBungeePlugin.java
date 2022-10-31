package dev.qrowned.punish.bungee;

import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class PunishBungeePlugin extends AbstractPunishPlugin {

    private final PunishBootstrap bootstrap;

}
