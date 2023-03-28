package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.config.api.ConfigService;
import dev.qrowned.config.message.bungee.BungeeMessageService;
import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.command.annotation.SubCommand;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.common.command.AbstractReloadSubCommand;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "punish", permission = "supremepunish.punish")
public final class PunishCommand extends AbstractPunishCommand<CommandSender> {

    public PunishCommand(@NotNull ConfigService configService,
                         @NotNull BungeeMessageService messageService,
                         @NotNull PunishUserDataHandler punishUserDataHandler,
                         @NotNull PunishmentDataHandler punishmentDataHandler) {
        PunishReloadSubCommand reloadSubCommand = new PunishReloadSubCommand(configService, messageService, punishUserDataHandler, punishmentDataHandler);
        this.registerSubCommands(reloadSubCommand);
    }

    @Override
    public void handleNoSubCommandFound(CommandSender sender, String[] args) {
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.handleNoSubCommandFound(sender, args);
    }

    @Override
    public boolean hasPermission(CommandSender player) {
        return player.hasPermission(super.getPermission());
    }

    @Override
    public UUID getUUID(CommandSender player) {
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

    @SubCommand(name = "reload", permission = "supremepunish.reload", boundToClass = PunishCommand.class)
    public static class PunishReloadSubCommand extends AbstractReloadSubCommand<CommandSender> {

        public PunishReloadSubCommand(ConfigService configService,
                                      BungeeMessageService messageService,
                                      PunishUserDataHandler punishUserDataHandler,
                                      PunishmentDataHandler punishmentDataHandler) {
            super(configService, messageService, punishUserDataHandler, punishmentDataHandler);
        }

        @Override
        public UUID getUUID(CommandSender player) {
            return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
        }
    }

}
