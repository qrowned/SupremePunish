package dev.qrowned.punish.velocity;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.punish.api.metrics.MetricsCompact;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import dev.qrowned.punish.velocity.bootstrap.PunishVelocityBootstrap;
import dev.qrowned.punish.velocity.command.VelocityCommandHandler;
import dev.qrowned.punish.velocity.command.impl.*;
import dev.qrowned.punish.velocity.listener.VelocityChatMessageListener;
import dev.qrowned.punish.velocity.listener.VelocityConnectionListener;
import dev.qrowned.punish.velocity.listener.punish.VelocityPardonListener;
import dev.qrowned.punish.velocity.listener.punish.VelocityPunishListener;
import dev.qrowned.punish.velocity.loader.PunishVelocityLoader;
import dev.qrowned.punish.velocity.message.VelocityMessageConfig;
import dev.qrowned.punish.velocity.message.VelocityMessageHandler;
import dev.qrowned.punish.velocity.metrics.VelocityMetrics;
import dev.qrowned.punish.velocity.user.VelocityPunishUserHandler;
import dev.qrowned.punish.velocity.user.transformer.VelocityPunishUserTransformer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class PunishVelocityPlugin extends AbstractPunishPlugin {

    @Getter
    private static ProxyServer server;

    private final PunishVelocityBootstrap bootstrap;

    private MetricsCompact.MetricsBase metricsBase;
    private VelocityMessageHandler messageHandler;
    private VelocityCommandHandler commandHandler;

    public PunishVelocityPlugin(@NotNull PunishVelocityBootstrap bootstrap) {
        super(new VelocityPlatform(bootstrap.getStartupTime()));
        this.bootstrap = bootstrap;
        server = bootstrap.getLoader().getProxyServer();
    }

    @Override
    public void load() {
        super.load();
        super.configProvider.registerConfig("messages", new File(PUNISH_FOLDER_PATH, "messages.json"), VelocityMessageConfig.class);
    }

    @Override
    public void registerHandler() {
        this.messageHandler = new VelocityMessageHandler(super.configProvider.getConfig("messages", VelocityMessageConfig.class));

        super.punishUserDataHandler = new PunishUserDataHandler(super.dataSource, new VelocityPunishUserTransformer());
        super.userHandler = new VelocityPunishUserHandler(super.punishUserDataHandler);

        this.metricsBase = new VelocityMetrics(
                this.bootstrap.getLoader(),
                this.bootstrap.getLoader().getProxyServer(),
                this.bootstrap.getPluginLogger(),
                this.bootstrap.getLoader().getDataDirectory(), 17071).getMetricsBase();

        super.registerHandler();
    }

    @Override
    protected void registerPlatformListener() {
        EventManager eventManager = server.getEventManager();
        PunishVelocityLoader loader = this.bootstrap.getLoader();

        eventManager.register(loader, new VelocityChatMessageListener(this.messageHandler, super.punishmentHandler));
        eventManager.register(loader, new VelocityConnectionListener(this, super.punishmentHandler, this.messageHandler));
    }

    @Override
    protected void registerCommands() {
        this.commandHandler = new VelocityCommandHandler(this.bootstrap.getLoader());
        this.commandHandler.registerCommands(
                new BanCommand(this.messageHandler, super.userHandler, super.punishmentHandler),
                new PunishCommand(super.configProvider, this.messageHandler, super.punishUserDataHandler, super.punishmentDataHandler),
                new UnbanCommand(this.messageHandler, super.userHandler, super.punishmentHandler),
                new MuteCommand(this.messageHandler, super.userHandler, super.punishmentHandler),
                new UnmuteCommand(this.messageHandler, super.userHandler, super.punishmentHandler),
                new KickCommand(this.messageHandler, super.userHandler, super.punishmentHandler),
                new PunishInfoCommand(this.messageHandler, super.userHandler, super.punishmentHandler),
                new HistoryCommand(super.userHandler, this.messageHandler, super.punishmentHandler)
        );
    }

    @Override
    public void registerPluginListener() {
        super.registerPluginListener();

        super.eventHandler.registerEventAdapter(
                new VelocityPunishListener(this.messageHandler, super.userHandler, super.punishmentDataHandler),
                new VelocityPardonListener(this.messageHandler, super.userHandler, super.punishmentDataHandler)
        );
    }
}
