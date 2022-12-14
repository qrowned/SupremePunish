package dev.qrowned.punish.velocity.message;

import com.velocitypowered.api.command.CommandSource;
import dev.qrowned.punish.api.exception.MessageNotFoundException;
import dev.qrowned.punish.api.message.AbstractConfigMessage;
import dev.qrowned.punish.api.message.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class VelocityMessageHandler implements MessageHandler<CommandSource> {

    private final VelocityMessageConfig messageConfig;

    @Override
    public @NotNull AbstractConfigMessage<CommandSource> getMessage(@NotNull String id) {
        return this.messageConfig.getMessages().stream()
                .filter(bungeeConfigMessage -> bungeeConfigMessage.getId().equals(id))
                .findFirst().orElseThrow(() -> new MessageNotFoundException(id));
    }

    @Override
    public @NotNull String getPrefix() {
        return this.messageConfig.getPrefix();
    }

}
