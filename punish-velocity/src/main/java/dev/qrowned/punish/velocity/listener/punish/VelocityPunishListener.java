package dev.qrowned.punish.velocity.listener.punish;

import com.velocitypowered.api.command.CommandSource;
import dev.qrowned.punish.api.event.EventListener;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.event.listener.AbstractPunishListener;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import dev.qrowned.punish.velocity.message.VelocityConfigMessage;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EventListener(clazz = PlayerPunishEvent.class)
public final class VelocityPunishListener extends AbstractPunishListener<CommandSource> {

    public VelocityPunishListener(VelocityMessageHandler messageHandler,
                                  PunishUserHandler punishUserHandler,
                                  PunishmentDataHandler punishmentDataHandler) {
        super(messageHandler, punishUserHandler, punishmentDataHandler);
    }

    @Override
    protected CommandSource getPlayer(@NotNull UUID uuid) {
        return PunishVelocityPlugin.getServer().getPlayer(uuid).orElse(null);
    }

    @Override
    protected void disconnect(@NotNull UUID uuid, @NotNull String messageId, String... format) {
        PunishVelocityPlugin.getServer().getPlayer(uuid).ifPresent(player -> {
            VelocityConfigMessage configMessage = (VelocityConfigMessage) super.messageHandler.getMessage(messageId);
            player.disconnect(configMessage.parseComponent(format));
        });
    }
}
