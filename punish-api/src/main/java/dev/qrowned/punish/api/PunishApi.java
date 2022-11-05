package dev.qrowned.punish.api;

import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.user.PunishUserHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PunishApi {

    @NotNull String getServerName();

    @NotNull ConfigProvider getConfigProvider();

    @NotNull PunishUserHandler getUserHandler();

}
