package dev.qrowned.punish.api;

import dev.qrowned.config.api.ConfigService;
import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.metrics.MetricsCompact;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import org.jetbrains.annotations.NotNull;

public interface PunishPlugin {

    @NotNull Platform getPlatform();

    PunishBootstrap getBootstrap();

    AbstractDataSource getDataSource();

    PluginLogger getLogger();

    @NotNull ConfigService getConfigService();

    @NotNull PunishUserHandler getUserHandler();

    @NotNull EventHandler getEventHandler();

    @NotNull PubSubProvider getPubSubProvider();

    @NotNull PunishmentHandler getPunishmentHandler();

    @NotNull CommandHandler<?> getCommandHandler();

    @NotNull MessageService<?> getMessageService();

    @NotNull MetricsCompact.MetricsBase getMetricsBase();

    boolean isChatLogAvailable();

}
