package dev.qrowned.punish.bungee.loader;

import dev.qrowned.punish.api.bootstrap.LoaderBootstrap;
import dev.qrowned.punish.bungee.bootstrap.PunishBungeeBootstrap;
import net.md_5.bungee.api.plugin.Plugin;

public final class PunishBungeeLoader extends Plugin {

    private final LoaderBootstrap loaderBootstrap = new PunishBungeeBootstrap(this);

    @Override
    public void onLoad() {
        this.loaderBootstrap.onLoad();
    }

    @Override
    public void onEnable() {
        this.loaderBootstrap.onEnable();
    }

    @Override
    public void onDisable() {
        this.loaderBootstrap.onDisable();
    }

}
