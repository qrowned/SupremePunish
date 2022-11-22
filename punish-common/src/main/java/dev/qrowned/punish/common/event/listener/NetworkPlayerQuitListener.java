package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.NetworkPlayerQuitEvent;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@EventListener(clazz = NetworkPlayerQuitEvent.class)
@RequiredArgsConstructor
public final class NetworkPlayerQuitListener extends EventAdapter<NetworkPlayerQuitEvent> {

    private final PunishUserDataHandler punishUserDataHandler;

    @Override
    public void handleReceive(@NotNull NetworkPlayerQuitEvent event) {
        this.punishUserDataHandler.invalidate(event.getUuid());
    }

}
