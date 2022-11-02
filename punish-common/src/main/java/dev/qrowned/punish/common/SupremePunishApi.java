package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.config.ConfigProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class SupremePunishApi implements PunishApi {

    private final PunishPlugin plugin;

    @Override
    public @NotNull String getServerName() {
        return this.plugin.getBootstrap().getServerName();
    }

    @Override
    public @NotNull ConfigProvider getConfigProvider() {
        return this.plugin.getConfigProvider();
    }

}
