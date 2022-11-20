package dev.qrowned.punish.bungee.listener;

import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.bungee.PunishBungeePlugin;
import dev.qrowned.punish.common.event.listener.AbstractConnectionListener;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

public final class BungeeConnectionListener extends AbstractConnectionListener implements Listener {

    private final PunishBungeePlugin plugin;
    private final PunishmentHandler punishmentHandler;

    public BungeeConnectionListener(@NotNull PunishBungeePlugin plugin, @NotNull PunishmentHandler punishmentHandler) {
        super(plugin);
        this.plugin = plugin;
        this.punishmentHandler = punishmentHandler;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(@NotNull LoginEvent event) {
        PendingConnection pendingConnection = event.getConnection();

        event.registerIntent(this.plugin.getBootstrap().getLoader());

        super.loadUser(pendingConnection.getUniqueId(), pendingConnection.getName()).thenAcceptAsync(punishUser -> {
            this.punishmentHandler.getActivePunishment(punishUser.getUuid(), Punishment.Type.BAN).thenAcceptAsync(punishmentOptional -> {
                if (punishmentOptional.isPresent()) {
                    Punishment punishment = punishmentOptional.get();
                    PunishmentReason punishmentReason = this.punishmentHandler.getPunishmentReason(punishment.getReason());
                    event.setCancelReason(TextComponent.fromLegacyText("You got banned with the reason " + (punishmentReason == null ? punishment.getReason() : punishmentReason.getDisplayName())));
                    event.setCancelled(true);
                }

                event.completeIntent(this.plugin.getBootstrap().getLoader());
            });
        });

        super.processJoin(pendingConnection.getUniqueId(), pendingConnection.getName());
    }

    @EventHandler
    public void handle(@NotNull PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        super.processQuit(player.getUniqueId(), player.getName());
    }

}
