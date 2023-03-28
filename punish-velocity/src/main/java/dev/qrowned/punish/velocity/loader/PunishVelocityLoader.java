package dev.qrowned.punish.velocity.loader;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.velocity.bootstrap.PunishVelocityBootstrap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(authors = "qrowned", version = "1.0.2", id = "supremepunish", name = "SupremePunish",
        dependencies = {
                @Dependency(id = "chatlog_velocity", optional = true)
        })
public class PunishVelocityLoader {

    private final LoaderBootstrap loaderBootstrap;

    @Getter
    private final ProxyServer proxyServer;
    @Getter
    private final Logger logger;
    @Getter
    private final Path dataDirectory;

    @Inject
    public PunishVelocityLoader(@NotNull ProxyServer proxyServer,
                                @NotNull Logger logger,
                                @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        this.loaderBootstrap = new PunishVelocityBootstrap(this);
        this.loaderBootstrap.onLoad();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.loaderBootstrap.onEnable();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.loaderBootstrap.onDisable();
    }

}
