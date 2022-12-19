package dev.qrowned.punish.velocity.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractUnbanCommand;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "unban", permission = "supremepunish.unban")
public final class UnbanCommand extends AbstractUnbanCommand<CommandSource> {

    public UnbanCommand(@NotNull VelocityMessageHandler messageHandler,
                        @NotNull PunishUserHandler punishUserHandler,
                        @NotNull PunishmentHandler punishmentHandler) {
        super(messageHandler, punishUserHandler, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSource sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSource player) {
        return player.hasPermission(super.getPermission());
    }

    @Override
    public UUID getUUID(CommandSource player) {
        return player instanceof Player player1 ? player1.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

}
