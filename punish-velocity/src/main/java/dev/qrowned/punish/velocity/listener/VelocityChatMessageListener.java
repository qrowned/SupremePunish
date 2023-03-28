package dev.qrowned.punish.velocity.listener;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import dev.qrowned.config.message.velocity.VelocityMessageService;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.common.event.listener.AbstractChatMessageListener;
import org.jetbrains.annotations.NotNull;

public final class VelocityChatMessageListener extends AbstractChatMessageListener<CommandSource> {

    public VelocityChatMessageListener(@NotNull VelocityMessageService messageService,
                                       @NotNull PunishmentHandler punishmentHandler) {
        super(messageService, punishmentHandler);
    }

    @Subscribe(order = PostOrder.FIRST)
    public EventTask onPlayerChat(@NotNull PlayerChatEvent event) {
        return EventTask.withContinuation(continuation -> {
            this.checkMuteStatus(event.getPlayer().getUniqueId(), event.getPlayer()).thenAccept(status -> {
                if (status) event.setResult(PlayerChatEvent.ChatResult.denied());
                continuation.resume();
            });
        });
    }

}
