package dev.qrowned.punish.bungee.bootstrap;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.api.exception.LicenseInvalidException;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.api.util.LicenseCheckerUtil;
import dev.qrowned.punish.bungee.PunishBungeePlugin;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import dev.qrowned.punish.common.logger.JavaPluginLogger;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

public final class PunishBungeeBootstrap implements PunishBootstrap, LoaderBootstrap {

    private final Plugin loader;
    private final PunishBungeePlugin plugin;

    private PluginLogger pluginLogger;
    private Instant startupTime;

    public PunishBungeeBootstrap(@NotNull Plugin loader) {
        this.loader = loader;
        this.plugin = new PunishBungeePlugin(this);
    }

    @Override
    @SneakyThrows
    public void onLoad() {
        this.pluginLogger = new JavaPluginLogger(this.loader.getLogger());
        this.startupTime = Instant.now();

        this.plugin.load();
    }

    @Override
    public void onEnable() {
        this.plugin.enable();
    }

    @Override
    public PluginLogger getPluginLogger() {
        if (this.pluginLogger == null)
            throw new IllegalStateException("Logger isn't initialised yet!");
        return this.pluginLogger;
    }

    @Override
    public String getVersion() {
        return this.loader.getDescription().getVersion();
    }

    @Override
    public String getServerVersion() {
        return this.loader.getProxy().getVersion();
    }

    @Override
    public Instant getStartupTime() {
        return this.startupTime;
    }

    @Override
    public Platform.Type getType() {
        return Platform.Type.BUNGEECORD;
    }

}
