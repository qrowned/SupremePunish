package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractBanCommand<P> extends AbstractPunishCommand<P> {

    private final MessageHandler<P> messageHandler;
    private final PunishUserHandler punishUserHandler;
    private final PunishmentHandler punishmentHandler;

    @Override
    public void execute(P sender, String[] args) {
        if (args.length == 0) return;
        UUID executor = this.getUUID(sender);

        this.punishUserHandler.fetchUser(args[0]).thenAcceptAsync(target -> {
            if (target == null) {
                this.messageHandler.getMessage("punish.user.notExisting").send(sender);
                return;
            }

            if (args.length == 2) {
                this.punishmentHandler.punish(target.getUuid(), executor, args[1]).thenAcceptAsync(result -> {
                    if (!result.isSuccess()) {
                        this.messageHandler.getMessage(result.getMessage()).send(sender);
                    }
                });
                return;
            } else if (args.length > 2 && this.hasPermission(sender, "supremepunish.ban.custom")) {
                long duration = this.parseTimeResult(args[1]);
                if (duration == 0) {
                    this.messageHandler.getMessage("punish.time.wrongFormat").send(sender);
                    return;
                }

                String reason = String.join(" ", ArrayUtils.subarray(args, 2, args.length));
                this.punishmentHandler.ban(target.getUuid(), executor, reason, duration).thenAcceptAsync(result -> {
                    if (!result.isSuccess()) {
                        this.messageHandler.getMessage(result.getMessage()).send(sender);
                    }
                });
                return;
            }

            this.handleNoSubCommandFound(sender, args);
        });
    }

    @Override
    public List<String> handleTabCompletion(P sender, String cursor, int currentArg, String[] args) {
        if (currentArg == 1) {
            return this.punishmentHandler.getPunishmentReasons().stream()
                    .filter(punishmentReason -> punishmentReason.getType().equals(Punishment.Type.BAN))
                    .filter(punishmentReason -> this.hasPermission(sender, punishmentReason.getPermission()))
                    .flatMap(punishmentReason -> punishmentReason.getUsableIds().stream())
                    .collect(Collectors.toList());
        }
        return super.handleTabCompletion(sender, cursor, currentArg, args);
    }


    protected abstract boolean hasPermission(@NotNull P player, @NotNull String permission);

    public long parseTimeResult(@NotNull String s) {
        try {
            String timeUnit = Character.toString(s.charAt(s.length() - 1));
            int timeValue = Integer.parseInt(s.substring(0, s.length() - 1));

            return switch (timeUnit.toLowerCase()) {
                case "y" -> TimeUnit.DAYS.toMillis(365) * timeValue;
                case "d" -> TimeUnit.DAYS.toMillis(timeValue);
                case "h" -> TimeUnit.HOURS.toMillis(timeValue);
                case "m" -> TimeUnit.MINUTES.toMillis(timeValue);
                case "s" -> TimeUnit.SECONDS.toMillis(timeValue);
                default -> 0;
            };
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

}
