package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.util.DurationFormatter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractPunishInfoCommand<P> extends AbstractPunishCommand<P> {

    private final MessageHandler<P> messageHandler;
    private final PunishUserHandler punishUserHandler;
    private final PunishmentHandler punishmentHandler;

    @Override
    public void execute(P sender, String[] args) {
        if (args.length == 0) {
            this.handleNoSubCommandFound(sender, args);
            return;
        }

        this.punishUserHandler.fetchUser(args[0]).thenAcceptAsync(abstractPunishUser -> {
            if (abstractPunishUser == null) {
                this.messageHandler.getMessage("punish.user.notExisting").send(sender);
                return;
            }

            UUID target = abstractPunishUser.getUuid();
            Optional<Punishment> optionalBan = this.punishmentHandler.getActivePunishment(target, Punishment.Type.BAN).join();
            Optional<Punishment> optionalMute = this.punishmentHandler.getActivePunishment(target, Punishment.Type.MUTE).join();

            Punishment ban = optionalBan.orElse(null);
            Punishment mute = optionalMute.orElse(null);
            this.messageHandler.getMessage("punish.user.info").send(sender,
                    "%name%", abstractPunishUser.getName(),
                    "%online%", abstractPunishUser.isOnline() ? "§aYes" : "§cNo",
                    "%uuid%", target.toString(),
                    "%banned%", ban == null
                            ? "§bNo"
                            : "§bYes (" + this.getReason(ban.getReason()) + " | " + DurationFormatter.formatPunishDuration(ban.getRemainingDuration()) + "§b)",
                    "%muted%", mute == null
                            ? "§bNo"
                            : "§bYes (" + this.getReason(mute.getReason()) + " | " + DurationFormatter.formatPunishDuration(mute.getRemainingDuration()) + "§b)"
            );
        });

    }

    private String getReason(@NotNull String id) {
        PunishmentReason reason = this.punishmentHandler.getPunishmentReason(id);
        return reason == null ? id : reason.getDisplayName();
    }

}
