package dev.qrowned.punish.velocity.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractKickCommand;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "kick", permission = "supremepunish.kick")
public final class KickCommand extends AbstractKickCommand<CommandSource> {

    public KickCommand(@NotNull VelocityMessageHandler messageHandler,
                       @NotNull PunishUserHandler punishUserHandler,
                       @NotNull PunishmentHandler punishmentHandler) {
        super(messageHandler, punishUserHandler, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSource sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSource player) {
        return this.hasPermission(player, this.getPermission());
    }

    @Override
    public UUID getUUID(CommandSource player) {
        return player instanceof Player player1 ? player1.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSource player, @NotNull String permission) {
        return player.hasPermission(permission);
    }

}
