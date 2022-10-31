package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.common.config.CommonConfigProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class SupremePunishApi implements PunishApi {

    private final PunishPlugin plugin;
    private final ConfigProvider configProvider = new CommonConfigProvider();

    @Override
    public @NotNull String getServerName() {
        return this.plugin.getBootstrap().getServerName();
    }
}
