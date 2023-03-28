package dev.qrowned.punish.common.command;

import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractUnbanCommand<P> extends AbstractPunishCommand<P> {

    private final MessageService<P> messageService;
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
                this.messageService.getMessage("punish.user.notExisting").send(sender);
                return;
            }

            String reason = args.length == 1 ? "-" : String.join(" ", ArrayUtils.subarray(args, 1, args.length));
            this.punishmentHandler.unban(target.getUuid(), executor, reason).thenAcceptAsync(result -> {
                if (!result.isSuccess()) {
                    this.messageService.getMessage(result.getMessage()).send(sender);
                } else {
                    this.messageService.getMessage("punish.unban.successful").send(sender, "%target%", target.getName());
                }
            });
        });
    }
}
