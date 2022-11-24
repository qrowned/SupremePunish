package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.util.DurationFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractMuteCommand<P> extends AbstractPunishCommand<P> {

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
                this.punishmentHandler.mute(target.getUuid(), executor, args[1]).thenAcceptAsync(result -> {
                    if (!result.isSuccess()) {
                        this.messageHandler.getMessage(result.getMessage()).send(sender);
                    }
                });
                return;
            } else if (args.length > 2 && this.hasPermission(sender, "supremepunish.mute.custom")) {
                long duration = DurationFormatter.parseDuration(args[1]);
                if (duration == 0) {
                    this.messageHandler.getMessage("punish.time.wrongFormat").send(sender);
                    return;
                }

                String reason = String.join(" ", ArrayUtils.subarray(args, 2, args.length));
                this.punishmentHandler.mute(target.getUuid(), executor, reason, duration).thenAcceptAsync(result -> {
                    if (!result.isSuccess()) {
                        this.messageHandler.getMessage(result.getMessage()).send(sender);
                    }
                });
                return;
            }
            this.handleNoSubCommandFound(sender, args);
        });
    }

    public abstract boolean hasPermission(P sender, @NotNull String permission);

}
