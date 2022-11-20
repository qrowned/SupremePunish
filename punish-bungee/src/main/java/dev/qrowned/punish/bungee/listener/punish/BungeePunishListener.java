package dev.qrowned.punish.bungee.listener.punish;

import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.common.event.listener.AbstractPunishListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EventListener(clazz = PlayerPunishEvent.class)
public class BungeePunishListener extends AbstractPunishListener {

    public BungeePunishListener(@NotNull PunishmentDataHandler punishmentDataHandler) {
        super(punishmentDataHandler);
    }

    @Override
    protected void disconnect(@NotNull UUID uuid, @NotNull String reason) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player == null) return;
        player.disconnect(TextComponent.fromLegacyText(reason));
    }

    @Override
    protected void broadcastMessage(@NotNull String message, @NotNull String permission) {
        ProxyServer.getInstance().getPlayers().stream()
                .filter(proxiedPlayer -> proxiedPlayer.hasPermission(permission))
                .forEach(proxiedPlayer -> proxiedPlayer.sendMessage(TextComponent.fromLegacyText(message)));
    }
}
