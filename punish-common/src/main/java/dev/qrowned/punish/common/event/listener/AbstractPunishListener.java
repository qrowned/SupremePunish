package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.util.DurationFormatter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractPunishListener extends EventAdapter<PlayerPunishEvent> {

    protected final MessageHandler<?> messageHandler;
    protected final PunishUserHandler punishUserHandler;
    protected final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void handleReceive(@NotNull PlayerPunishEvent event) {
        UUID target = event.getTarget();
        UUID executor = event.getExecutor();

        this.punishmentDataHandler.invalidate(target);

        AbstractPunishUser targetUser = this.punishUserHandler.getUser(target);
        AbstractPunishUser executorUser = this.punishUserHandler.getUser(executor);

        if (targetUser == null || executorUser == null) return;

        Punishment punishment = event.getPunishment();
        if (punishment.getType().equals(Punishment.Type.BAN)) {
            String displayName = event.getPunishmentReason().getDisplayName();
            this.disconnect(target, "punish.ban.screen",
                    "%reason%", displayName,
                    "%end%", DurationFormatter.formatPunishDuration(punishment.getRemainingDuration()),
                    "%id%", Integer.toString(punishment.getId())
            );
            this.messageHandler.getMessage("punish.ban.notify").broadcast("supremepunish.notify.ban",
                    "%reason%", displayName,
                    "%duration%", DurationFormatter.formatPunishDuration(punishment.getDuration()),
                    "%executor%", executorUser.getName(),
                    "%target%", targetUser.getName(),
                    "%id%", Integer.toString(punishment.getId()));
        }
    }

    protected abstract void disconnect(@NotNull UUID uuid, @NotNull String messageId, String... format);

}
