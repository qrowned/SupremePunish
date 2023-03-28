package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.config.message.bungee.BungeeMessageService;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractUnmuteCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "unmute", permission = "supremepunish.unmute")
public final class UnmuteCommand extends AbstractUnmuteCommand<CommandSender> {

    public UnmuteCommand(@NotNull BungeeMessageService messageService,
                         @NotNull PunishUserHandler punishUserHandler,
                         @NotNull PunishmentHandler punishmentHandler) {
        super(messageService, punishUserHandler, punishmentHandler);
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
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

}
