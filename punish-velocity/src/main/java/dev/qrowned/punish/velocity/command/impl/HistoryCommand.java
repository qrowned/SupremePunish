package dev.qrowned.punish.velocity.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.config.message.velocity.VelocityConfigMessage;
import dev.qrowned.config.message.velocity.VelocityMessageService;
import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.command.AbstractHistoryCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Command(name = "history", permission = "supremepunish.history")
public final class HistoryCommand extends AbstractHistoryCommand<CommandSource> {

    public HistoryCommand(@NotNull PunishUserHandler userHandler,
                          @NotNull VelocityMessageService messageService,
                          @NotNull PunishmentHandler punishmentHandler) {
        super(userHandler, messageService, punishmentHandler);
    }

    @Override
    public void handleNoSubCommandFound(CommandSource sender, String[] args) {
    }

    @Override
    public boolean hasPermission(CommandSource player) {
        return player.hasPermission(this.getPermission());
    }

    @Override
    public UUID getUUID(CommandSource player) {
        return player instanceof Player player1 ? player1.getUniqueId() : AbstractPunishUser.CONSOLE_UUID;
    }

    @Override
    protected void sendFooterMessage(CommandSource sender, int remaining, int currentPage, String name) {
        VelocityConfigMessage message = (VelocityConfigMessage) super.messageService.getMessage("punish.history.footer");
        Component component = message.parseComponent("%remaining%", Integer.toString(remaining))
                .clickEvent(ClickEvent.runCommand("/history " + name + " " + (currentPage + 1)));
        sender.sendMessage(component);
    }

}
