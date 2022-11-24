package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.common.util.DurationFormatter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class AbstractChatMessageListener<P> {

    private final MessageHandler<P> messageHandler;
    private final PunishmentHandler punishmentHandler;

    public CompletableFuture<Boolean> checkMuteStatus(@NotNull UUID uuid, P sender) {
        return this.punishmentHandler.getActivePunishment(uuid, Punishment.Type.MUTE).thenApplyAsync(punishmentOptional -> {
            if (punishmentOptional.isEmpty()) return false;

            Punishment punishment = punishmentOptional.get();
            PunishmentReason punishmentReason = this.punishmentHandler.getPunishmentReason(punishment.getReason());
            this.messageHandler.getMessage("punish.mute.screen").send(sender,
                    "%reason%", punishmentReason == null ? punishment.getReason() : punishmentReason.getDisplayName(),
                    "%id%", Integer.toString(punishment.getId()),
                    "%end%", DurationFormatter.formatPunishDuration(punishment.getRemainingDuration()));
            return true;
        });
    }

}
