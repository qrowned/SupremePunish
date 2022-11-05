package dev.qrowned.punish.api;

import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.user.PunishUserHandler;
import org.jetbrains.annotations.NotNull;

public interface PunishPlugin {

    PunishBootstrap getBootstrap();

    AbstractDataSource getDataSource();

    PluginLogger getLogger();

    @NotNull ConfigProvider getConfigProvider();

    @NotNull PunishUserHandler getUserHandler();

}
