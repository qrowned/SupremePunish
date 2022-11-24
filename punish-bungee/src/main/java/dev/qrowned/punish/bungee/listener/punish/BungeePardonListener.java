package dev.qrowned.punish.bungee.listener.punish;

import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.bungee.message.BungeeMessageHandler;
import dev.qrowned.punish.common.event.listener.AbstractPardonListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EventListener(clazz = PlayerPardonEvent.class)
public final class BungeePardonListener extends AbstractPardonListener<CommandSender> {

    public BungeePardonListener(@NotNull BungeeMessageHandler messageHandler,
                                @NotNull PunishUserHandler punishUserHandler,
                                @NotNull PunishmentDataHandler punishmentDataHandler) {
        super(messageHandler, punishUserHandler, punishmentDataHandler);
    }


    @Override
    public CommandSender getPlayer(@NotNull UUID uuid) {
        return ProxyServer.getInstance().getPlayer(uuid);
    }

}
