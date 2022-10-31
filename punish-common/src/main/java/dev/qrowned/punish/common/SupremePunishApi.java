package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.common.config.CommonConfigProvider;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SupremePunishApi implements PunishApi {

    private final ConfigProvider configProvider = new CommonConfigProvider();

    @Override
    public @NotNull String getServerName() {
        return null;
    }

}
