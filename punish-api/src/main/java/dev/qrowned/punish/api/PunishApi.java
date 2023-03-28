package dev.qrowned.punish.api;

import dev.qrowned.config.api.ConfigService;
import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import org.jetbrains.annotations.NotNull;

public interface PunishApi {

    @NotNull Platform getPlatform();

    @NotNull String getServerName();

    @NotNull ConfigService getConfigService();

    @NotNull PunishUserHandler getUserHandler();

    @NotNull EventHandler getEventHandler();

    @NotNull PubSubProvider getPubSubProvider();

    @NotNull PunishmentHandler getPunishmentHandler();

    @NotNull CommandHandler<?> getCommandHandler();

    @NotNull MessageService<?> getMessageService();

}
