package dev.qrowned.punish.bungee;

import dev.qrowned.punish.bungee.bootstrap.PunishBungeeBootstrap;
import dev.qrowned.punish.bungee.command.BungeeCommandHandler;
import dev.qrowned.punish.bungee.command.impl.BanCommand;
import dev.qrowned.punish.bungee.command.impl.ReloadCommand;
import dev.qrowned.punish.bungee.command.impl.TestCommand;
import dev.qrowned.punish.bungee.listener.BungeeConnectionListener;
import dev.qrowned.punish.bungee.listener.punish.BungeePunishListener;
import dev.qrowned.punish.bungee.user.BungeePunishUserHandler;
import dev.qrowned.punish.bungee.user.transformer.BungeePunishUserTransformer;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.PluginManager;

@Getter
@RequiredArgsConstructor
public final class PunishBungeePlugin extends AbstractPunishPlugin {

    private final PunishBungeeBootstrap bootstrap;

    private BungeeCommandHandler commandHandler;

    @Override
    public void registerHandler() {
        super.punishUserDataHandler = new PunishUserDataHandler(super.dataSource, new BungeePunishUserTransformer());
        super.userHandler = new BungeePunishUserHandler(super.punishUserDataHandler);

        super.registerHandler();
    }

    @Override
    protected void registerPlatformListener() {
        PluginManager pluginManager = this.bootstrap.getProxy().getPluginManager();
        pluginManager.registerListener(this.bootstrap.getLoader(), new BungeeConnectionListener(this, super.punishmentHandler));
    }

    @Override
    protected void registerCommands() {
        this.commandHandler = new BungeeCommandHandler(this.bootstrap.getLoader());
        this.commandHandler.registerCommands(
                new TestCommand(), new BanCommand(super.userHandler, super.punishmentHandler),
                new ReloadCommand(super.configProvider, super.punishUserDataHandler, super.punishmentDataHandler)
        );
    }

    @Override
    public void registerPluginListener() {
        super.registerPluginListener();

        super.eventHandler.registerEventAdapter(new BungeePunishListener(super.punishmentDataHandler));
    }
}
