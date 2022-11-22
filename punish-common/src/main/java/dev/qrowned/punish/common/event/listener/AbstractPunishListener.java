package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractPunishListener extends EventAdapter<PlayerPunishEvent> {

    protected final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void handleReceive(@NotNull PlayerPunishEvent event) {
        this.punishmentDataHandler.invalidate(event.getTarget());

        this.disconnect(event.getTarget(), "You got banned with the reason " + event.getPunishmentReason().getDisplayName());
        this.broadcastMessage("[PUNISH] " + event.getTarget() + " got punished (" + event.getPunishment().getType() + ") by " + event.getExecutor() + " with the reason " + event.getPunishmentReason().getDisplayName(),
                "supremepunish.punish.notify");
    }

    protected abstract void disconnect(@NotNull UUID uuid, @NotNull String message);

    protected abstract void broadcastMessage(@NotNull String message, @NotNull String permission);

}
