package dev.qrowned.punish.bukkit.bootstrap;

import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.api.bootstrap.PunishBootstrap;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.bukkit.PunishBukkitPlugin;
import dev.qrowned.punish.common.logger.JavaPluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public final class PunishBukkitBootstrap implements PunishBootstrap, LoaderBootstrap {

    private final JavaPlugin loader;
    // private final PunishBukkitPlugin plugin;

    private PluginLogger pluginLogger;
    private Instant startupTime;

    public PunishBukkitBootstrap(@NotNull JavaPlugin loader) {
        this.loader = loader;
        // this.plugin = new PunishBukkitPlugin();
    }

    @Override
    public void onLoad() {
        this.pluginLogger = new JavaPluginLogger(this.loader.getLogger());
        this.startupTime = Instant.now();


    }

    @Override
    public PluginLogger getPluginLogger() {
        if (this.pluginLogger == null)
            throw new IllegalStateException("Logger isn't initialised yet!");
        return this.pluginLogger;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public String getServerVersion() {
        return null;
    }

    @Override
    public Instant getStartupTime() {
        return null;
    }

    @Override
    public Platform.Type getType() {
        return null;
    }

}
