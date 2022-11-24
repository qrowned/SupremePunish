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
public abstract class AbstractPunishListener<P> extends EventAdapter<PlayerPunishEvent> {

    protected final MessageHandler<P> messageHandler;
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
        String displayName = event.getPunishmentReason().getDisplayName();
        if (punishment.getType().equals(Punishment.Type.BAN)) {
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
        } else if (punishment.getType().equals(Punishment.Type.MUTE)) {
            P targetPlayer = this.getPlayer(target);
            if (targetPlayer != null)
                this.messageHandler.getMessage("punish.mute.screen").send(targetPlayer,
                        "%reason%", displayName,
                        "%end%", DurationFormatter.formatPunishDuration(punishment.getRemainingDuration()),
                        "%id%", Integer.toString(punishment.getId())
                );
            this.messageHandler.getMessage("punish.mute.notify").broadcast("supremepunish.notify.mute",
                    "%reason%", displayName,
                    "%duration%", DurationFormatter.formatPunishDuration(punishment.getDuration()),
                    "%executor%", executorUser.getName(),
                    "%target%", targetUser.getName(),
                    "%id%", Integer.toString(punishment.getId()));
        } else if (punishment.getType().equals(Punishment.Type.KICK)) {
            this.disconnect(target, "punish.kick.screen",
                    "%reason%", displayName,
                    "%id%", Integer.toString(punishment.getId())
            );
            this.messageHandler.getMessage("punish.kick.notify").broadcast("supremepunish.notify.kick",
                    "%reason%", displayName,
                    "%executor%", executorUser.getName(),
                    "%target%", targetUser.getName(),
                    "%id%", Integer.toString(punishment.getId())
            );
        }
    }

    protected abstract P getPlayer(@NotNull UUID uuid);

    protected abstract void disconnect(@NotNull UUID uuid, @NotNull String messageId, String... format);

}
