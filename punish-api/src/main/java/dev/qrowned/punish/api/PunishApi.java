package dev.qrowned.punish.api;

import dev.qrowned.punish.api.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PunishApi {

    @NotNull String getServerName();

    @NotNull ConfigProvider getConfigProvider();

}
