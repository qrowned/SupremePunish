package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractBanCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "ban", aliases = "banhammer", permission = "supremepunish.ban")
public final class BanCommand extends AbstractBanCommand<CommandSender> {

    public BanCommand(@NotNull PunishUserHandler punishUserHandler,
                      @NotNull PunishmentHandler punishmentHandler) {
        super(punishUserHandler, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSender sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSender player) {
        return player.hasPermission(this.getPermission());
    }

    @Override
    public UUID getUUID(CommandSender player) {
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : new UUID(0, 0);
    }

    @Override
    protected boolean hasPermission(@NotNull CommandSender player, @NotNull String permission) {
        return player.hasPermission(permission);
    }
}
