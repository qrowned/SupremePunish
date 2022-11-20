package dev.qrowned.punish.api;

import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import org.jetbrains.annotations.NotNull;

public interface PunishPlugin {

    PunishBootstrap getBootstrap();

    AbstractDataSource getDataSource();

    PluginLogger getLogger();

    @NotNull ConfigProvider getConfigProvider();

    @NotNull PunishUserHandler getUserHandler();

    @NotNull EventHandler getEventHandler();

    @NotNull PubSubProvider getPubSubProvider();

    @NotNull PunishmentHandler getPunishmentHandler();

    @NotNull CommandHandler<?> getCommandHandler();

}
