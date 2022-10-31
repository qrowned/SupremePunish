package dev.qrowned.punish.bungee.bootstrap;

import dev.qrowned.punish.common.bootstrap.CommonPunishBootstrap;
import dev.qrowned.punish.common.logger.JavaPluginLogger;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class PunishBungeeBootstrap extends CommonPunishBootstrap {

    private final Plugin loader;

    public PunishBungeeBootstrap(@NotNull Plugin loader) {
        this.loader = loader;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        super.setPluginLogger(new JavaPluginLogger(this.loader.getLogger()));
    }

}
