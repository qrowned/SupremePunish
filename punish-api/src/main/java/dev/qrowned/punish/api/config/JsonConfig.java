package dev.qrowned.punish.api.config;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

@Getter
public final class JsonConfig<T> {

    @Setter
    private static Gson GSON = new Gson();

    private final String configName;
    private final File file;
    private final Class<T> clazz;
    private T config;

    @SneakyThrows
    public JsonConfig(String configName, File file, Class<T> clazz) {
        this.configName = configName;
        this.file = file;
        this.clazz = clazz;
        this.reload();
    }

    @SneakyThrows
    public JsonConfig(String configName, Class<T> clazz) {
        this.configName = configName;
        this.file = new File(configName);
        this.clazz = clazz;
        this.reload();
    }

    public void reload() throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.file.createNewFile();
            this.config = (T) this.clazz.getConstructors()[0].newInstance();

            InputStream inputStream = this.getResource(this.configName + ".json");
            if (inputStream != null) {
                FileUtils.copyInputStreamToFile(inputStream, this.file);
            } else {
                this.save();
            }
        }

        final String json = new String(Files.readAllBytes(this.file.toPath()));
        this.config = GSON.fromJson(json, this.clazz);
    }

    public void save() throws IOException {
        try (Writer writer = new FileWriter(this.file)) {
            GSON.toJson(this.config, writer);
        }
    }

    private InputStream getResource(@NotNull String filename) {
        try {
            URL url = this.clazz.getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }

}