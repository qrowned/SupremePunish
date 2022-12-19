package dev.qrowned.punish.velocity.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.command.annotation.SubCommand;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.common.command.AbstractReloadSubCommand;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import dev.qrowned.punish.velocity.command.VelocityCommandHandler;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "punish", permission = "supremepunish.punish")
public final class PunishCommand extends AbstractPunishCommand<CommandSource> {

    public PunishCommand(@NotNull ConfigProvider configProvider,
                         @NotNull VelocityMessageHandler messageHandler,
                         @NotNull PunishUserDataHandler punishUserDataHandler,
                         @NotNull PunishmentDataHandler punishmentDataHandler) {
        PunishReloadSubCommand reloadSubCommand = new PunishReloadSubCommand(configProvider, messageHandler, punishUserDataHandler, punishmentDataHandler);
        this.registerSubCommands(reloadSubCommand);
    }

    @Override
    public void handleNoSubCommandFound(CommandSource sender, String[] args) {
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        this.handleNoSubCommandFound(sender, args);
    }

    @Override
    public boolean hasPermission(CommandSource player) {
        return player.hasPermission(super.getPermission());
    }

    @Override
    public UUID getUUID(CommandSource player) {
        return player instanceof Player player1 ? player1.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

    @SubCommand(name = "reload", permission = "supremepunish.reload", boundToClass = PunishCommand.class)
    public static class PunishReloadSubCommand extends AbstractReloadSubCommand<CommandSource> {

        public PunishReloadSubCommand(ConfigProvider configProvider,
                                      VelocityMessageHandler messageHandler,
                                      PunishUserDataHandler punishUserDataHandler,
                                      PunishmentDataHandler punishmentDataHandler) {
            super(configProvider, messageHandler, punishUserDataHandler, punishmentDataHandler);
        }

        @Override
        public UUID getUUID(CommandSource player) {
            return player instanceof Player player1 ? player1.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
        }
    }

}
