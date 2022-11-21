package dev.qrowned.punish.api.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ConfigProvider {

    <T extends ConfigAdapter<T>> T getConfig(@NotNull String name, Class<T> clazz);

    default <T extends ConfigAdapter<T>> T registerConfig(@NotNull String fileName, Class<T> clazz) {
        return this.registerConfig(fileName, new File(fileName), clazz);
    }

    <T extends ConfigAdapter<T>> T registerConfig(@NotNull String name, @NotNull File file, Class<T> clazz);

    void reloadConfigs();

}
