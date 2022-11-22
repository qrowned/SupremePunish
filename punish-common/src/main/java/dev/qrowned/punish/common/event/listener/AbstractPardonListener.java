package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class AbstractPardonListener extends EventAdapter<PlayerPardonEvent> {

    private final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void handleReceive(@NotNull PlayerPardonEvent event) {
        this.punishmentDataHandler.invalidate(event.getTarget());

        this.broadcastMessage("[PUNISH] " + event.getTarget() + " got pardon (" + event.getPunishment().getType() + ") by " + event.getExecutor() + " with the reason " + event.getPunishment().getPardonReason(),
                "supremepunish.pardon.notify");
    }

    protected abstract void broadcastMessage(@NotNull String message, @NotNull String permission);

}
