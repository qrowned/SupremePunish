package dev.qrowned.punish.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.metrics.MetricsCompact;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import dev.qrowned.punish.velocity.bootstrap.PunishVelocityBootstrap;
import dev.qrowned.punish.velocity.message.VelocityMessageConfig;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import dev.qrowned.punish.velocity.metrics.VelocityMetrics;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class PunishVelocityPlugin extends AbstractPunishPlugin {

    @Getter
    private static ProxyServer server;

    private final PunishVelocityBootstrap bootstrap;

    private MetricsCompact.MetricsBase metricsBase;
    private VelocityMessageHandler messageHandler;

    public PunishVelocityPlugin(@NotNull PunishVelocityBootstrap bootstrap) {
        super(new VelocityPlatform(bootstrap.getStartupTime()));
        this.bootstrap = bootstrap;
        server = bootstrap.getLoader().getProxyServer();
    }

    @Override
    public @NotNull CommandHandler<?> getCommandHandler() {
        return null;
    }

    @Override
    public void load() {
        super.load();
        super.configProvider.registerConfig("messages", new File(PUNISH_FOLDER_PATH, "messages.json"), VelocityMessageConfig.class);
    }

    @Override
    public void registerHandler() {
        this.messageHandler = new VelocityMessageHandler(super.configProvider.getConfig("messages", VelocityMessageConfig.class));

        this.metricsBase = new VelocityMetrics(
                this.bootstrap.getLoader(),
                this.bootstrap.getLoader().getProxyServer(),
                this.bootstrap.getPluginLogger(),
                this.bootstrap.getLoader().getDataDirectory(), 17071).getMetricsBase();

        super.registerHandler();
    }

    @Override
    protected void registerPlatformListener() {

    }

    @Override
    protected void registerCommands() {

    }
}
