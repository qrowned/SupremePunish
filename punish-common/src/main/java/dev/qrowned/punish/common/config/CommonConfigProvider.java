package dev.qrowned.punish.common.config;

import dev.qrowned.punish.api.config.ConfigAdapter;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.config.JsonConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CommonConfigProvider implements ConfigProvider {

    private final List<JsonConfig<?>> jsonConfigList = new ArrayList<>();

    @Override
    public <T extends ConfigAdapter<T>> T getConfig(@NotNull String name, Class<T> clazz) {
        JsonConfig<?> jsonConfig = this.jsonConfigList.stream().filter(config -> config.getConfigName().equalsIgnoreCase(name))
                .filter(config -> config.getClazz().equals(clazz))
                .findFirst().orElse(null);
        if (jsonConfig == null) return null;

        return (T) jsonConfig.getConfig();
    }

    @Override
    public <T extends ConfigAdapter<T>> T registerConfig(@NotNull String name, @NotNull File file, Class<T> clazz) {
        JsonConfig<T> jsonConfig = new JsonConfig<T>(name, file, clazz);
        this.jsonConfigList.add(jsonConfig);
        return jsonConfig.getConfig();
    }

    @Override
    public void reloadConfigs() {
        this.jsonConfigList.forEach(jsonConfig -> {
            try {
                jsonConfig.reload();
            } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
