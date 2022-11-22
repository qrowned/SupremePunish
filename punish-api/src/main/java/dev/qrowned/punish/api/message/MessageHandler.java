package dev.qrowned.punish.api.message;

import org.jetbrains.annotations.NotNull;

public interface MessageHandler<P> {

    @NotNull AbstractConfigMessage<P> getMessage(@NotNull String id);

    @NotNull String getPrefix();

}
