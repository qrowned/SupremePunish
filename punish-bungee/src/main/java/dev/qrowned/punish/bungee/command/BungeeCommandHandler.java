package dev.qrowned.punish.bungee.command;

import dev.qrowned.punish.api.command.AbstractCommandHandler;
import dev.qrowned.punish.api.command.PunishCommand;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class BungeeCommandHandler extends AbstractCommandHandler<CommandSender> {

    private final Plugin plugin;

    @Override
    protected void registerPlatformInternalCommand(@NotNull PunishCommand<CommandSender> punishCommand) {
        BungeeCommandAdapter commandAdapter = new BungeeCommandAdapter(punishCommand);
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, commandAdapter);
    }

}
