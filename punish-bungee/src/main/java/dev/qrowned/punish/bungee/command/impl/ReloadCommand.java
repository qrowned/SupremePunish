package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.common.command.AbstractReloadCommand;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "reload", aliases = "", permission = "supremepunish.reload")
public final class ReloadCommand extends AbstractReloadCommand<CommandSender> {

    public ReloadCommand(@NotNull ConfigProvider configProvider,
                         @NotNull PunishUserDataHandler punishUserDataHandler,
                         @NotNull PunishmentDataHandler punishmentDataHandler) {
        super(configProvider, punishUserDataHandler, punishmentDataHandler);
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
        return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : new UUID(0, 0);
    }

    @Override
    protected void sendMessage(@NotNull CommandSender player, @NotNull String message) {
        player.sendMessage(TextComponent.fromLegacyText(message));
    }
}
