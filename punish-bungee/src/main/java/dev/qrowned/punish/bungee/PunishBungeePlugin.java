package dev.qrowned.punish.bungee;

import dev.qrowned.punish.bungee.bootstrap.PunishBungeeBootstrap;
import dev.qrowned.punish.bungee.listener.BungeeConnectionListener;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.PluginManager;

@Getter
@RequiredArgsConstructor
public final class PunishBungeePlugin extends AbstractPunishPlugin {

    private final PunishBungeeBootstrap bootstrap;

    @Override
    protected void registerListener() {
        PluginManager pluginManager = this.bootstrap.getProxy().getPluginManager();
        pluginManager.registerListener(this.bootstrap.getLoader(), new BungeeConnectionListener(this));
    }

    @Override
    protected void registerCommands() {

    }
}
