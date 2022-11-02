package dev.qrowned.punish.common.config;

import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.config.JsonConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommonConfigProvider implements ConfigProvider {

    private final List<JsonConfig<?>> jsonConfigList = new ArrayList<>();

    @Override
    public <T> T getConfig(@NotNull String name, Class<T> clazz) {
        JsonConfig<?> jsonConfig = this.jsonConfigList.stream().filter(config -> config.getConfigName().equalsIgnoreCase(name))
                .filter(config -> config.getClazz().equals(clazz))
                .findFirst().orElse(null);
        if (jsonConfig == null) return null;

        return (T) jsonConfig.getConfig();
    }

    @Override
    public <T> T registerConfig(@NotNull String name, @NotNull File file, Class<T> clazz) {
        JsonConfig<T> jsonConfig = new JsonConfig<T>(name, file, clazz);
        this.jsonConfigList.add(jsonConfig);
        return jsonConfig.getConfig();
    }

}
