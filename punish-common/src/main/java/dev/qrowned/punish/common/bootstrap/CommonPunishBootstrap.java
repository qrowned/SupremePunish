package dev.qrowned.punish.common.bootstrap;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.api.exception.LicenseInvalidException;
import dev.qrowned.license.client.LicenseHandler;
import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.PunishApiProvider;
import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.common.SupremePunishApi;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Getter
public class CommonPunishBootstrap implements PunishBootstrap, LoaderBootstrap {

    private PunishApi punishApi;
    private Instant startupTime;
    @Setter
    private PluginLogger pluginLogger;

    private final Platform.Type type;
    private final String version;
    private final String serverVersion;
    private final String serverName;

    private final LicenseHandler licenseHandler = new LicenseHandler(UUID.fromString("41adc24e-e5db-4357-8fe5-79bb7f50e69a"));

    public CommonPunishBootstrap(Platform.Type type,
                                 @NotNull String version,
                                 String serverVersion,
                                 String serverName) {
        this.type = type;
        this.version = version;
        this.serverVersion = serverVersion;
        this.serverName = serverName;
    }

    @Override
    @SneakyThrows
    public void onLoad() {
        this.punishApi = new SupremePunishApi(this.serverName);
        this.checkLicense();

        PunishApiProvider.register(this.punishApi);
    }

    @Override
    public void onEnable() {
        this.startupTime = Instant.now();
    }

    @Override
    public @Nullable PluginLogger getPluginLogger() {
        if (this.pluginLogger == null)
            throw new IllegalStateException("Logger has not been initialised yet");
        return this.pluginLogger;
    }

    private void checkLicense() throws IOException, LicenseInvalidException {
        LicenseConfig licenseConfig = this.punishApi.getConfigProvider().getConfig("license.json", LicenseConfig.class);
        LicenseData licenseData = this.licenseHandler.authenticate(licenseConfig.getLicense());
        if (licenseData == null) {
            this.pluginLogger.severe("Your license is not valid! Please purchase the plugin or visit our support at url.qrowned.dev/discord !");
            throw new LicenseInvalidException(licenseConfig.getLicense());
        } else {
            this.pluginLogger.info("Your license is valid! | Expiration: " + licenseData.getExpirationDate());
        }
    }

}
