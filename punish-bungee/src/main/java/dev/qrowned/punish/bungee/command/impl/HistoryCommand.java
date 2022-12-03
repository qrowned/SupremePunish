package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.message.AbstractConfigMessage;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.bungee.message.BungeeConfigMessage;
import dev.qrowned.punish.bungee.message.BungeeMessageHandler;
import dev.qrowned.punish.common.command.AbstractHistoryCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "history", permission = "supremepunish.history")
public final class HistoryCommand extends AbstractHistoryCommand<CommandSender> {

    public HistoryCommand(@NotNull PunishUserHandler userHandler,
                          @NotNull BungeeMessageHandler messageHandler,
                          @NotNull PunishmentHandler punishmentHandler) {
        super(userHandler, messageHandler, punishmentHandler);
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

    @Override
    protected void sendFooterMessage(CommandSender sender, int remaining, int currentPage, String name) {
        BungeeConfigMessage message = (BungeeConfigMessage) super.messageHandler.getMessage("punish.history.footer");
        BaseComponent[] baseComponents = message.parseBaseComponent("%remaining%", Integer.toString(remaining));
        for (BaseComponent baseComponent : baseComponents) {
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history " + name + " " + (currentPage + 1)));
        }
        sender.sendMessage(baseComponents);
    }
}
