package dev.qrowned.punish.bungee.listener.punish;

import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.common.event.listener.AbstractPardonListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

@EventListener(clazz = PlayerPardonEvent.class)
public final class BungeePardonListener extends AbstractPardonListener {

    public BungeePardonListener(PunishmentDataHandler punishmentDataHandler) {
        super(punishmentDataHandler);
    }

    @Override
    protected void broadcastMessage(@NotNull String message, @NotNull String permission) {
        ProxyServer.getInstance().getPlayers().stream()
                .filter(proxiedPlayer -> proxiedPlayer.hasPermission(permission))
                .forEach(proxiedPlayer -> proxiedPlayer.sendMessage(TextComponent.fromLegacyText(message)));
    }

}
