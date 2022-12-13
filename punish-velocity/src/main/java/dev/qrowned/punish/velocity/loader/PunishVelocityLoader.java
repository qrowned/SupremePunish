package dev.qrowned.punish.velocity.loader;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.velocity.bootstrap.PunishVelocityBootstrap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Plugin(authors = "qrowned", version = "1.0", id = "supremepunish")
public class PunishVelocityLoader {

    private final LoaderBootstrap loaderBootstrap;

    @Inject
    public PunishVelocityLoader(@NotNull ProxyServer proxyServer, @NotNull Logger logger) {
        this.loaderBootstrap = new PunishVelocityBootstrap(proxyServer, logger);
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
