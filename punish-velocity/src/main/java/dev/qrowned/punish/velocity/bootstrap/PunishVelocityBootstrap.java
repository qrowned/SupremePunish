package dev.qrowned.punish.velocity.bootstrap;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.api.exception.LicenseInvalidException;
import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import dev.qrowned.punish.common.logger.SLF4JPlugginLogger;
import dev.qrowned.punish.common.util.LicenseCheckerUtil;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import dev.qrowned.punish.velocity.loader.PunishVelocityLoader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Getter
public final class PunishVelocityBootstrap implements PunishBootstrap, LoaderBootstrap {

    private final PunishVelocityLoader loader;

    private final PluginLogger pluginLogger;
    private final PunishVelocityPlugin plugin;

    private Instant startupTime;

    public PunishVelocityBootstrap(@NotNull PunishVelocityLoader loader) {
        this.loader = loader;
        this.pluginLogger = new SLF4JPlugginLogger(loader.getLogger());
        this.plugin = new PunishVelocityPlugin(this);
    }

    @Override
    public void onLoad() {
        this.startupTime = Instant.now();

        this.plugin.load();
    }

    @Override
    @SneakyThrows
    public void onEnable() {
        // check license
        LicenseConfig licenseConfig = this.plugin.getConfigProvider().getConfig("license", LicenseConfig.class);
        LicenseData licenseData = LicenseCheckerUtil.checkLicense(licenseConfig.getLicense());
        if (licenseData == null) {
            this.pluginLogger.severe("Your license is not valid! Please purchase the plugin or visit our support at url.qrowned.dev/discord !");
            throw new LicenseInvalidException(licenseConfig.getLicense());
        } else {
            this.pluginLogger.info("Your license is valid! | Expiration: " + licenseData.getExpirationDate());
        }

        this.plugin.enable();
    }

    @Override
    public PluginLogger getPluginLogger() {
        return this.pluginLogger;
    }

    @Override
    public String getVersion() {
        return this.loader.getProxyServer().getPluginManager().getPlugin("supremepunish").orElseThrow().getDescription().getVersion().orElseThrow();
    }

    @Override
    public String getServerVersion() {
        return this.loader.getProxyServer().getVersion().getVersion();
    }

    @Override
    public Instant getStartupTime() {
        return this.startupTime;
    }

    @Override
    public Platform.Type getType() {
        return this.plugin.getPlatform().getType();
    }

}