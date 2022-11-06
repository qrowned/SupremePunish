package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@EventListener(clazz = PlayerPunishEvent.class)
@RequiredArgsConstructor
public abstract class AbstractPunishListener extends EventAdapter<PlayerPunishEvent> {

    private final PluginLogger pluginLogger;
    private final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void handleReceive(@NotNull PlayerPunishEvent event) {
        this.punishmentDataHandler.invalidate(event.getTarget());
    }

}
