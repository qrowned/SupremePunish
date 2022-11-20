package dev.qrowned.punish.bungee.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public abstract class BungeePunishCommand extends AbstractPunishCommand<CommandSender> {

    @Override
    public void handleNoSubCommandFound(CommandSender sender, String[] args) {

    }

    @Override
    public boolean hasPermission(CommandSender player) {
        return player.hasPermission(super.getPermission());
    }

    @Override
    public UUID getUUID(CommandSender player) {
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : new UUID(0, 0);
    }

}
