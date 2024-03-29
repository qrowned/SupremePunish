package dev.qrowned.punish.bungee.listener.punish;

import dev.qrowned.config.message.bungee.BungeeConfigMessage;
import dev.qrowned.config.message.bungee.BungeeMessageService;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.event.listener.AbstractPunishListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EventListener(clazz = PlayerPunishEvent.class)
public final class BungeePunishListener extends AbstractPunishListener<CommandSender> {

    public BungeePunishListener(@NotNull BungeeMessageService messageService,
                                @NotNull PunishUserHandler punishUserHandler,
                                @NotNull PunishmentDataHandler punishmentDataHandler) {
        super(messageService, punishUserHandler, punishmentDataHandler);
    }

    @Override
    protected CommandSender getPlayer(@NotNull UUID uuid) {
        return ProxyServer.getInstance().getPlayer(uuid);
    }

    @Override
    protected void disconnect(@NotNull UUID uuid, @NotNull String messageId, String... format) {
        BungeeConfigMessage configMessage = (BungeeConfigMessage) super.messageService.getMessage(messageId);
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
        if (proxiedPlayer == null) return;
        proxiedPlayer.disconnect(configMessage.parseBaseComponent(format));
    }

}
