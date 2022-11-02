package dev.qrowned.punish.api.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ConfigProvider {

    <T> T getConfig(@NotNull String name, Class<T> clazz);

    default <T> T registerConfig(@NotNull String fileName, Class<T> clazz) {
        return this.registerConfig(fileName, new File(fileName), clazz);
    }

    <T> T registerConfig(@NotNull String name, @NotNull File file, Class<T> clazz);

}
