package dev.qrowned.punish.bungee.listener.punish;

import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.common.event.listener.AbstractPunishListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import org.jetbrains.annotations.NotNull;

public class BungeePunishListener extends AbstractPunishListener {

    public BungeePunishListener(PluginLogger pluginLogger, PunishmentDataHandler punishmentDataHandler) {
        super(pluginLogger, punishmentDataHandler);
    }

    @Override
    public void handleReceive(@NotNull PlayerPunishEvent event) {
        super.handleReceive(event);

        // TODO: 06.11.2022 Implement ban screen
    }
}
