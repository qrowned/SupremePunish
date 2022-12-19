package dev.qrowned.punish.velocity.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractPunishInfoCommand;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "punishinfo", aliases = {
        "info"
}, permission = "supremepunish.info")
public final class PunishInfoCommand extends AbstractPunishInfoCommand<CommandSource> {

    public PunishInfoCommand(@NotNull VelocityMessageHandler messageHandler,
                             @NotNull PunishUserHandler punishUserHandler,
                             @NotNull PunishmentHandler punishmentHandler) {
        super(messageHandler, punishUserHandler, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSource sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSource player) {
        return player.hasPermission(this.getPermission());
    }

    @Override
    public UUID getUUID(CommandSource player) {
        return player instanceof Player player1 ? player1.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

}
