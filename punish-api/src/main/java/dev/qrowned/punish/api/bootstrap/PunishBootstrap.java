package dev.qrowned.punish.api.bootstrap;

import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.platform.Platform;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface PunishBootstrap {

    PluginLogger getPluginLogger();

    String getVersion();

    String getServerVersion();

    Instant getStartupTime();

    Platform.Type getType();

    default @Nullable String getServerName() {
        return null;
    }

}
