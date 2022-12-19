package dev.qrowned.punish.velocity.listener.punish;

import com.velocitypowered.api.command.CommandSource;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.event.listener.AbstractPardonListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EventListener(clazz = PlayerPardonEvent.class)
public final class VelocityPardonListener extends AbstractPardonListener<CommandSource> {

    public VelocityPardonListener(@NotNull VelocityMessageHandler messageHandler,
                                  @NotNull PunishUserHandler punishUserHandler,
                                  @NotNull PunishmentDataHandler punishmentDataHandler) {
        super(messageHandler, punishUserHandler, punishmentDataHandler);
    }

    @Override
    public CommandSource getPlayer(@NotNull UUID uuid) {
        return PunishVelocityPlugin.getServer().getPlayer(uuid).orElse(null);
    }

}
