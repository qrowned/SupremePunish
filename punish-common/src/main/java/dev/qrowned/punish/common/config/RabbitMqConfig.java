package dev.qrowned.punish.common.config;

import dev.qrowned.config.api.ConfigAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
public final class RabbitMqConfig implements ConfigAdapter<RabbitMqConfig> {

    private String host;
    private int port;
    private String username;
    private String password;
    private String virtualHost;
    private int connectionTimeout;

    @Override
    public void reload(@NotNull RabbitMqConfig config) {
        this.host = config.getHost();
        this.port = config.getPort();
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.virtualHost = config.getVirtualHost();
        this.connectionTimeout = config.getConnectionTimeout();
    }

}
