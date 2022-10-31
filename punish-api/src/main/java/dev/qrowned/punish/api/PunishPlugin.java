package dev.qrowned.punish.api;

import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.database.AbstractDataSource;

public interface PunishPlugin {

    PunishBootstrap getBootstrap();

    AbstractDataSource getDataSource();

}
