package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.common.config.CommonConfigProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SupremePunishApi implements PunishApi {

    private final String serverName;
    private final ConfigProvider configProvider = new CommonConfigProvider();

}
