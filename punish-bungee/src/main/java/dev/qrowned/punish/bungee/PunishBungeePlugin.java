package dev.qrowned.punish.bungee;

import dev.qrowned.punish.api.metrics.MetricsCompact;
import dev.qrowned.punish.bungee.bootstrap.PunishBungeeBootstrap;
import dev.qrowned.punish.bungee.command.BungeeCommandHandler;
import dev.qrowned.punish.bungee.command.impl.*;
import dev.qrowned.punish.bungee.listener.BungeeChatMessageListener;
import dev.qrowned.punish.bungee.listener.BungeeConnectionListener;
import dev.qrowned.punish.bungee.listener.punish.BungeePardonListener;
import dev.qrowned.punish.bungee.listener.punish.BungeePunishListener;
import dev.qrowned.punish.bungee.message.BungeeMessageConfig;
import dev.qrowned.punish.bungee.message.BungeeMessageHandler;
import dev.qrowned.punish.bungee.metrics.BungeeMetrics;
import dev.qrowned.punish.bungee.user.BungeePunishUserHandler;
import dev.qrowned.punish.bungee.user.transformer.BungeePunishUserTransformer;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import lombok.Getter;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class PunishBungeePlugin extends AbstractPunishPlugin {

    private final PunishBungeeBootstrap bootstrap;

    private MetricsCompact.MetricsBase metricsBase;
    private BungeeCommandHandler commandHandler;
    private BungeeMessageHandler messageHandler;

    public PunishBungeePlugin(@NotNull PunishBungeeBootstrap bootstrap) {
        super(new BungeeCordPlatform(bootstrap.getStartupTime()));
        this.bootstrap = bootstrap;
    }

    @Override
    public void load() {
        super.load();
        super.configProvider.registerConfig("messages", new File(PUNISH_FOLDER_PATH, "messages.json"), BungeeMessageConfig.class);
    }

    @Override
    public void registerHandler() {
        this.messageHandler = new BungeeMessageHandler(super.configProvider.getConfig("messages", BungeeMessageConfig.class));

        super.punishUserDataHandler = new PunishUserDataHandler(super.dataSource, new BungeePunishUserTransformer());
        super.userHandler = new BungeePunishUserHandler(super.punishUserDataHandler);

        this.metricsBase = new BungeeMetrics(this.bootstrap.getLoader(), 17070).getMetricsBase();

        super.registerHandler();
    }

    @Override
    protected void registerPlatformListener() {
        PluginManager pluginManager = this.bootstrap.getProxy().getPluginManager();
        pluginManager.registerListener(this.bootstrap.getLoader(), new BungeeConnectionListener(this, super.punishmentHandler, this.messageHandler));
        pluginManager.registerListener(this.bootstrap.getLoader(), new BungeeChatMessageListener(this.messageHandler, super.punishmentHandler));
    }

    @Override
    protected void registerCommands() {
        this.commandHandler = new BungeeCommandHandler(this.bootstrap.getLoader());
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
                new BungeePunishListener(this.messageHandler, super.userHandler, super.punishmentDataHandler),
                new BungeePardonListener(this.messageHandler, super.userHandler, super.punishmentDataHandler)
        );
    }
}
