package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.NetworkPlayerJoinEvent;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@EventListener(clazz = NetworkPlayerJoinEvent.class)
@RequiredArgsConstructor
public final class NetworkPlayerJoinListener extends EventAdapter<NetworkPlayerJoinEvent> {

    private final PunishPlugin plugin;

    @Override
    public void handleReceive(@NotNull NetworkPlayerJoinEvent event) {
        this.plugin.getPlatform().getUniqueConnections().add(event.getUuid());
    }

}
