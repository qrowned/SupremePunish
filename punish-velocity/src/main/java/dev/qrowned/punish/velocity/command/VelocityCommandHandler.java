package dev.qrowned.punish.velocity.command;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import dev.qrowned.punish.api.command.AbstractCommandHandler;
import dev.qrowned.punish.api.command.PunishCommand;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import dev.qrowned.punish.velocity.loader.PunishVelocityLoader;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class VelocityCommandHandler extends AbstractCommandHandler<CommandSource> {

    private final PunishVelocityLoader loader;

    @Override
    protected void registerPlatformInternalCommand(@NotNull PunishCommand<CommandSource> punishCommand) {
        CommandManager commandManager = PunishVelocityPlugin.getServer().getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(punishCommand.getName())
                .aliases(punishCommand.getAliases())
                .plugin(this.loader)
                .build();
        VelocityCommandAdapter commandAdapter = new VelocityCommandAdapter(punishCommand);
        commandManager.register(commandMeta, commandAdapter);
    }

}
