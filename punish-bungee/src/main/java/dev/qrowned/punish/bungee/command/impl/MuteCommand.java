package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.config.message.bungee.BungeeMessageService;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractMuteCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "mute", permission = "supremepunish.mute")
public final class MuteCommand extends AbstractMuteCommand<CommandSender> {

    public MuteCommand(@NotNull BungeeMessageService messageService,
                       @NotNull PunishUserHandler punishUserHandler,
                       @NotNull PunishmentHandler punishmentHandler) {
        super(messageService, punishUserHandler, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSender sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSender player) {
        return this.hasPermission(player, this.getPermission());
    }

    @Override
    public UUID getUUID(CommandSender player) {
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

    @Override
    public boolean hasPermission(CommandSender sender, @NotNull String permission) {
        return sender.hasPermission(permission);
    }
}
