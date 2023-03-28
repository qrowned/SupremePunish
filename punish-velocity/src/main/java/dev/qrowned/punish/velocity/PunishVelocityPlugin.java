package dev.qrowned.punish.velocity;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.config.message.velocity.VelocityMessageService;
import dev.qrowned.config.message.velocity.api.VelocityMessageApi;
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
import dev.qrowned.punish.velocity.metrics.VelocityMetrics;
import dev.qrowned.punish.velocity.user.VelocityPunishUserHandler;
import dev.qrowned.punish.velocity.user.transformer.VelocityPunishUserTransformer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PunishVelocityPlugin extends AbstractPunishPlugin {

    @Getter
    private static ProxyServer server;

    private final PunishVelocityBootstrap bootstrap;

    private MetricsCompact.MetricsBase metricsBase;
    private VelocityMessageApi messageApi;
    private VelocityCommandHandler commandHandler;

    public PunishVelocityPlugin(@NotNull PunishVelocityBootstrap bootstrap) {
        super(new VelocityPlatform(bootstrap.getStartupTime()));
        this.bootstrap = bootstrap;
        server = bootstrap.getLoader().getProxyServer();
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void registerHandler() {
        this.messageApi = new VelocityMessageApi(this.configService, server);

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

        eventManager.register(loader, new VelocityChatMessageListener(this.getMessageService(), super.punishmentHandler));
        eventManager.register(loader, new VelocityConnectionListener(this, super.punishmentHandler, this.getMessageService()));
    }

    @Override
    protected void registerCommands() {
        this.commandHandler = new VelocityCommandHandler(this.bootstrap.getLoader());
        this.commandHandler.registerCommands(
                new BanCommand(this.getMessageService(), super.userHandler, super.punishmentHandler),
                new PunishCommand(super.configService, this.getMessageService(), super.punishUserDataHandler, super.punishmentDataHandler),
                new UnbanCommand(this.getMessageService(), super.userHandler, super.punishmentHandler),
                new MuteCommand(this.getMessageService(), super.userHandler, super.punishmentHandler),
                new UnmuteCommand(this.getMessageService(), super.userHandler, super.punishmentHandler),
                new KickCommand(this.getMessageService(), super.userHandler, super.punishmentHandler),
                new PunishInfoCommand(this.getMessageService(), super.userHandler, super.punishmentHandler),
                new HistoryCommand(super.userHandler, this.getMessageService(), super.punishmentHandler)
        );
    }

    @Override
    public void registerPluginListener() {
        super.registerPluginListener();

        super.eventHandler.registerEventAdapter(
                new VelocityPunishListener(this.getMessageService(), super.userHandler, super.punishmentDataHandler),
                new VelocityPardonListener(this.getMessageService(), super.userHandler, super.punishmentDataHandler)
        );
    }

    @Override
    public @NotNull VelocityMessageService getMessageService() {
        return this.messageApi.getMessageService();
    }

    @Override
    public boolean isChatLogAvailable() {
        return server.getPluginManager().getPlugin("chatlog_velocity").isPresent();
    }

}
