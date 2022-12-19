package dev.qrowned.punish.velocity.listener;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.common.event.listener.AbstractChatMessageListener;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import org.jetbrains.annotations.NotNull;

public final class VelocityChatMessageListener extends AbstractChatMessageListener<CommandSource> {

    public VelocityChatMessageListener(@NotNull VelocityMessageHandler messageHandler,
                                       @NotNull PunishmentHandler punishmentHandler) {
        super(messageHandler, punishmentHandler);
    }

    @Subscribe(order = PostOrder.FIRST)
    public EventTask onPlayerChat(@NotNull PlayerChatEvent event) {
        return EventTask.withContinuation(continuation -> {
            Boolean status = this.checkMuteStatus(event.getPlayer().getUniqueId(), event.getPlayer()).join();
            if (status) event.setResult(PlayerChatEvent.ChatResult.denied());
        });
    }

}
