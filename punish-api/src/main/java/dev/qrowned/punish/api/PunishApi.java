package dev.qrowned.punish.api;

import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import org.jetbrains.annotations.NotNull;

public interface PunishApi {

    @NotNull String getServerName();

    @NotNull ConfigProvider getConfigProvider();

    @NotNull PunishUserHandler getUserHandler();

    @NotNull EventHandler getEventHandler();

    @NotNull PubSubProvider getPubSubProvider();

    @NotNull PunishmentHandler getPunishmentHandler();

    @NotNull CommandHandler<?> getCommandHandler();

    @NotNull MessageHandler<?> getMessageHandler();

}
