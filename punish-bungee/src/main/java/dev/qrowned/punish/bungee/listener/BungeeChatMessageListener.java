package dev.qrowned.punish.bungee.listener;

import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.bungee.message.BungeeMessageHandler;
import dev.qrowned.punish.common.event.listener.AbstractChatMessageListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

public final class BungeeChatMessageListener extends AbstractChatMessageListener<CommandSender> implements Listener {

    public BungeeChatMessageListener(@NotNull BungeeMessageHandler bungeeMessageHandler,
                                     @NotNull PunishmentHandler punishmentHandler) {
        super(bungeeMessageHandler, punishmentHandler);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(@NotNull ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer proxiedPlayer)) return;

        // TODO: 24.11.2022 Find a way to handle async
        Boolean muteStatus = this.checkMuteStatus(proxiedPlayer.getUniqueId(), proxiedPlayer).join();
        if (muteStatus) event.setCancelled(true);
    }

}
