package dev.qrowned.punish.bungee;

import dev.qrowned.chatlog.api.ChatLogApi;
import dev.qrowned.config.message.bungee.BungeeMessageService;
import dev.qrowned.config.message.bungee.api.BungeeMessageApi;
import dev.qrowned.punish.api.metrics.MetricsCompact;
import dev.qrowned.punish.bungee.bootstrap.PunishBungeeBootstrap;
import dev.qrowned.punish.bungee.command.BungeeCommandHandler;
import dev.qrowned.punish.bungee.command.impl.*;
import dev.qrowned.punish.bungee.listener.BungeeChatMessageListener;
import dev.qrowned.punish.bungee.listener.BungeeConnectionListener;
import dev.qrowned.punish.bungee.listener.punish.BungeePardonListener;
import dev.qrowned.punish.bungee.listener.punish.BungeePunishListener;
import dev.qrowned.punish.bungee.metrics.BungeeMetrics;
import dev.qrowned.punish.bungee.user.BungeePunishUserHandler;
import dev.qrowned.punish.bungee.user.transformer.BungeePunishUserTransformer;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import lombok.Getter;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

@Getter
public final class PunishBungeePlugin extends AbstractPunishPlugin {

    private final PunishBungeeBootstrap bootstrap;

    private MetricsCompact.MetricsBase metricsBase;
    private BungeeCommandHandler commandHandler;
    private BungeeMessageApi messageApi;

    public PunishBungeePlugin(@NotNull PunishBungeeBootstrap bootstrap) {
        super(new BungeeCordPlatform(bootstrap.getStartupTime()));
        this.bootstrap = bootstrap;
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void registerHandler() {
        this.messageApi = new BungeeMessageApi(super.configService);

        super.punishUserDataHandler = new PunishUserDataHandler(super.dataSource, new BungeePunishUserTransformer());
        super.userHandler = new BungeePunishUserHandler(super.punishUserDataHandler);

        this.metricsBase = new BungeeMetrics(this.bootstrap.getLoader(), 17070).getMetricsBase();

        super.registerHandler();
    }

    @Override
    protected void registerPlatformListener() {
        PluginManager pluginManager = this.bootstrap.getProxy().getPluginManager();
        pluginManager.registerListener(this.bootstrap.getLoader(), new BungeeConnectionListener(this, super.punishmentHandler, this.getMessageService()));
        pluginManager.registerListener(this.bootstrap.getLoader(), new BungeeChatMessageListener(this.getMessageService(), super.punishmentHandler));
    }

    @Override
    protected void registerCommands() {
        this.commandHandler = new BungeeCommandHandler(this.bootstrap.getLoader());
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
                new BungeePunishListener(this.getMessageService(), super.userHandler, super.punishmentDataHandler),
                new BungeePardonListener(this.getMessageService(), super.userHandler, super.punishmentDataHandler)
        );
    }

    @Override
    public Optional<ChatLogApi> getChatLogApi() {
        return Optional.ofNullable(this.chatLogApi);
    }

    @Override
    public @NotNull BungeeMessageService getMessageService() {
        return this.messageApi.getMessageService();
    }

    @Override
    public boolean isChatLogAvailable() {
        return this.bootstrap.getProxy().getPluginManager().getPlugin("Chatlog") != null;
    }

}
