package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractKickCommand<P> extends AbstractPunishCommand<P> {

    private final MessageHandler<P> messageHandler;
    private final PunishUserHandler punishUserHandler;
    private final PunishmentHandler punishmentHandler;

    @Override
    public void execute(P sender, String[] args) {
        if (args.length == 0) {
            this.handleNoSubCommandFound(sender, args);
            return;
        }
        UUID executor = this.getUUID(sender);

        this.punishUserHandler.fetchUser(args[0]).thenAcceptAsync(target -> {
            if (target == null) {
                this.messageHandler.getMessage("punish.user.notExisting").send(sender);
                return;
            }

            if (args.length == 2) {
                String reason = String.join(" ", ArrayUtils.subarray(args, 1, args.length));
                PunishmentReason punishmentReason = this.punishmentHandler.getPunishmentReason(reason, Punishment.Type.KICK);
                if (punishmentReason != null) {
                    this.punishmentHandler.punish(target.getUuid(), executor, punishmentReason).thenAcceptAsync(result -> {
                        if (!result.isSuccess()) {
                            this.messageHandler.getMessage(result.getMessage()).send(sender);
                        }
                    });
                    return;
                } else if (this.hasPermission(sender, "supremepunish.kick.custom")) {
                    this.punishmentHandler.kick(target.getUuid(), executor, reason).thenAcceptAsync(result -> {
                        if (!result.isSuccess()) {
                            this.messageHandler.getMessage(result.getMessage()).send(sender);
                        }
                    });
                    return;
                }
            }
            this.handleNoSubCommandFound(sender, args);
        });
    }

    public abstract boolean hasPermission(P sender, @NotNull String permission);

}
