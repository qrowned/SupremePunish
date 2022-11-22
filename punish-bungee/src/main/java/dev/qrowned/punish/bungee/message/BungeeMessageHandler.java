package dev.qrowned.punish.bungee.message;

import dev.qrowned.punish.api.exception.MessageNotFoundException;
import dev.qrowned.punish.api.message.AbstractConfigMessage;
import dev.qrowned.punish.api.message.MessageHandler;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class BungeeMessageHandler implements MessageHandler<CommandSender> {

    private final BungeeMessageConfig messageConfig;

    @Override
    public @NotNull AbstractConfigMessage<CommandSender> getMessage(@NotNull String id) {
        return this.messageConfig.getMessages().stream()
                .filter(bungeeConfigMessage -> bungeeConfigMessage.getId().equals(id))
                .findFirst().orElseThrow(() -> new MessageNotFoundException(id));
    }

    @Override
    public @NotNull String getPrefix() {
        return this.messageConfig.getPrefix();
    }

}
