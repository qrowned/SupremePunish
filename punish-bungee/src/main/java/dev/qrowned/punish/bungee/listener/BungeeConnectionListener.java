package dev.qrowned.punish.bungee.listener;

import dev.qrowned.punish.bungee.PunishBungeePlugin;
import dev.qrowned.punish.common.util.AbstractConnectionListener;
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

    public BungeeConnectionListener(@NotNull PunishBungeePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(@NotNull LoginEvent event) {
        PendingConnection pendingConnection = event.getConnection();

        event.registerIntent(this.plugin.getBootstrap().getLoader());

        super.loadUser(pendingConnection.getUniqueId(), pendingConnection.getName()).thenAcceptAsync(punishUser -> {
            // TODO: 05.11.2022 Implement ban mechanism

            event.completeIntent(this.plugin.getBootstrap().getLoader());
        });

        super.processJoin(pendingConnection.getUniqueId(), pendingConnection.getName());
    }

    @EventHandler
    public void handle(@NotNull PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        super.processQuit(player.getUniqueId(), player.getName());
    }

}
