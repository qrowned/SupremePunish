package dev.qrowned.punish.velocity.bootstrap;

import com.velocitypowered.api.proxy.ProxyServer;
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
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.time.Instant;

public final class PunishVelocityBootstrap implements PunishBootstrap, LoaderBootstrap {

    @Getter
    private final ProxyServer server;
    private final PunishVelocityPlugin plugin;

    private final PluginLogger pluginLogger;
    private Instant startupTime;

    public PunishVelocityBootstrap(@NotNull ProxyServer server, @NotNull Logger logger) {
        this.server = server;
        this.plugin = new PunishVelocityPlugin(this);
        this.pluginLogger = new SLF4JPlugginLogger(logger);
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
        return this.server.getPluginManager().getPlugin("supremepunish").orElseThrow().getDescription().getVersion().orElseThrow();
    }

    @Override
    public String getServerVersion() {
        return this.server.getVersion().getVersion();
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