package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractUnbanCommand<P> extends AbstractPunishCommand<P> {

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

            String reason = args.length == 1 ? "-" : String.join(" ", ArrayUtils.subarray(args, 1, args.length));
            this.punishmentHandler.pardon(target.getUuid(), executor, Punishment.Type.BAN, reason).thenAcceptAsync(result -> {
                if (!result.isSuccess()) {
                    this.messageHandler.getMessage(result.getMessage()).send(sender);
                } else {
                    this.messageHandler.getMessage("punish.unban.successful").send(sender, "%target%", target.getName());
                }
            });
        });
    }
}
