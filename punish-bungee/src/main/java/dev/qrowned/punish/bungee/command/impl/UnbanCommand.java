package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.config.message.bungee.BungeeMessageService;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractUnbanCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Command(name = "unban", permission = "supremepunish.unban")
public final class UnbanCommand extends AbstractUnbanCommand<CommandSender> {

    public UnbanCommand(BungeeMessageService messageService,
                        PunishUserHandler punishUserHandler,
                        PunishmentHandler punishmentHandler) {
        super(messageService, punishUserHandler, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSender sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSender player) {
        return player.hasPermission(super.getPermission());
    }

    @Override
    public UUID getUUID(CommandSender player) {
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

}
