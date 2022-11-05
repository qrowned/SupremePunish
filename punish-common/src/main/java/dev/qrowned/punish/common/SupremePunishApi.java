package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
public class SupremePunishApi implements PunishApi {

    private final PunishPlugin plugin;

    private final PunishUserHandler userHandler;

    public SupremePunishApi(@NotNull PunishPlugin plugin) {
        this.plugin = plugin;

        this.userHandler = plugin.getUserHandler();
    }

    @Override
    public @NotNull String getServerName() {
        return this.plugin.getBootstrap().getServerName();
    }

    @Override
    public @NotNull ConfigProvider getConfigProvider() {
        return this.plugin.getConfigProvider();
    }

}
