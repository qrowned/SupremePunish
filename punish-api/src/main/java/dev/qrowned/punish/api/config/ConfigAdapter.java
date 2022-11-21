package dev.qrowned.punish.api.config;

import org.jetbrains.annotations.NotNull;

public interface ConfigAdapter<T extends ConfigAdapter> {

    void reload(@NotNull T config);

}
